package com.hyeongarl.service;

import com.hyeongarl.entity.Category;
import com.hyeongarl.error.CategoryNotFoundException;
import com.hyeongarl.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // Look Aside : 캐시를 먼저 조회하고 데이터 없는 경우 DB에서 로드후 캐시에 저장
    @Cacheable(value = "category", key = "#userId")
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
    /** Async 비교 **/
    @CachePut(value = "category", key = "#userId")
    public CompletableFuture<Category> updateCategory(Category category, Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            Category existCategory = categoryRepository.findByUserId(userId)
                    .orElseThrow(CategoryNotFoundException::new);
            existCategory.setCategoryTree(category.getCategoryTree() != null ?
                    category.getCategoryTree() : existCategory.getCategoryTree());

            return categoryRepository.save(existCategory);
        });
    }

    /** Async 비교 **/
    @CachePut(value = "category", key = "#userId")
    public CompletableFuture<Category> updateCategoryAsync(Category category, Long userId) {
        long startTime = System.currentTimeMillis();

        return CompletableFuture.supplyAsync(() -> {
            Category existCategory = categoryRepository.findByUserId(userId)
                    .orElseThrow(CategoryNotFoundException::new);
            existCategory.setCategoryTree(category.getCategoryTree() != null ?
                    category.getCategoryTree() : existCategory.getCategoryTree());

            Category result = categoryRepository.save(existCategory);

            long endTime = System.currentTimeMillis();
            log.info("Async Service Time : {}", (endTime - startTime));

            return result;
        });
    }

    /** Sync 비교 **/
    @CachePut(value = "category", key = "#userId")
    public Category updateCategorySync(Category category, Long userId) {
        long startTime = System.currentTimeMillis();
        Category existCategory = categoryRepository.findByUserId(userId)
                .orElseThrow(CategoryNotFoundException::new);
        existCategory.setCategoryTree(category.getCategoryTree() != null ?
                category.getCategoryTree() : existCategory.getCategoryTree());

        Category result = categoryRepository.save(existCategory);

        long endTime = System.currentTimeMillis();
        log.info("Sync Service Time : {}", (endTime - startTime));

        return result;
    }

    @CacheEvict(value = "category", key = "#userId")
    public void deleteCategory(Long categoryId, Long userId) {
        Category existCategory = categoryRepository.findByUserId(userId)
                .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(existCategory);
    }
}
