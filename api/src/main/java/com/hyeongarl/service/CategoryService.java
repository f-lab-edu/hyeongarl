package com.hyeongarl.service;

import com.hyeongarl.entity.Category;
import com.hyeongarl.error.CategoryNotFoundException;
import com.hyeongarl.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CacheManager cacheManager;

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

    @CachePut(value = "category", key = "#userId")
    public Category updateCategory(Category category, Long userId) {
        Category existCategory = categoryRepository.findByUserId(userId)
                .orElseThrow(CategoryNotFoundException::new);
        existCategory.setCategoryTree(category.getCategoryTree() != null ?
                category.getCategoryTree() : existCategory.getCategoryTree());
        return categoryRepository.save(existCategory);
    }

    @CacheEvict(value = "category", key = "#userId")
    public void deleteCategory(Long categoryId, Long userId) {
        Category existCategory = categoryRepository.findByUserId(userId)
                .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(existCategory);
    }

}
