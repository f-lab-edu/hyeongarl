package com.hyeongarl.service;

import com.hyeongarl.entity.Category;
import com.hyeongarl.error.CategoryNotFoundException;
import com.hyeongarl.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Executor customPool;

    @Autowired
    private CacheManager cacheManager;

    // Look Aside : 캐시를 먼저 조회하고 데이터 없는 경우 DB에서 로드후 캐시에 저장
    @Cacheable(value = "category", key = "#userId", unless = "#result == null")
    public Category getCategory(Long userId) {
        return categoryRepository.findByUserId(userId)
                .orElseThrow(CategoryNotFoundException::new);
    }

    // Write Through: 데이터 업데이트시 캐시와 데이터베이스를 동시에 업데이트
    @CachePut(value = "category", key = "#userId")
    public Category save(Category category, Long userId) {
        category.setUserId(userId);
        return categoryRepository.save(category);
    }

    public CompletableFuture<Category> updateCategory(Category category, Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            Category existCategory = categoryRepository.findByUserId(userId)
                    .orElseThrow(CategoryNotFoundException::new);
            existCategory.setCategoryTree(category.getCategoryTree() != null ?
                    category.getCategoryTree() : existCategory.getCategoryTree());

            Category savedCategory = categoryRepository.save(existCategory);

            cacheManager.getCache("category").put(userId, savedCategory);
            return savedCategory;
        }, customPool);
    }

    @CacheEvict(value = "category", key = "#userId")
    public void deleteCategory(Long categoryId, Long userId) {
        Category existCategory = categoryRepository.findByUserId(userId)
                .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(existCategory);
    }

    // 로그인 시, 사용자 카테고리 캐시에 업로드
    @KafkaListener(topics = "login-topic", groupId = "login-upload")
    public void loadCategory(Long userId) {
        try {
            Category category = categoryRepository.findByUserId(userId)
                    .orElseThrow(CategoryNotFoundException::new);
            cacheManager.getCache("category").put(userId, category);
        } catch (NumberFormatException e) {
            log.error("유효하지 않은 userId 형식입니다: {}", userId, e);
        }
    }
}
