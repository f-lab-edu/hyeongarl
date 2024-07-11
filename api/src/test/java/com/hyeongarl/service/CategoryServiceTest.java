package com.hyeongarl.service;

import com.hyeongarl.dto.CategoryRequestDto;
import com.hyeongarl.entity.Category;
import com.hyeongarl.error.CategoryNotFoundException;
import com.hyeongarl.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private final Map<String, Object> categoryTree = createSampleCategoryTree();
    private final CategoryRequestDto categoryRequest = CategoryRequestDto.builder()
            .categoryTree(categoryTree)
            .build();
    private final Category category = Category.builder()
            .categoryTree(categoryTree)
            .userId(1L)
            .categoryId(1L)
            .build();
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("delete_success")
    void deleteCategory_success() {
        when(categoryRepository.findByUserId(anyLong())).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L, 1L);

        verify(categoryRepository, times(1)).findByUserId(anyLong());
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    private Map<String, Object> createSampleCategoryTree() {
        Map<String, Object> rootNode = new HashMap<>();
        rootNode.put("name", "root");

        Map<String, Object> childNode1 = new HashMap<>();
        childNode1.put("name", "child1");

        Map<String, Object> childNode2 = new HashMap<>();
        childNode2.put("name", "child2");

        List<Map<String, Object>> children = new ArrayList<>();
        children.add(childNode1);
        children.add(childNode2);

        rootNode.put("children", children);

        return rootNode;
    }

    @Nested
    @DisplayName("Category 등록")
    class saveCategoryTests {
        @Test
        @DisplayName("save_success")
        void saveCategory_success() {
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            Category category = categoryService.save(categoryRequest.toEntity(), 1L);

            assertNotNull(category);
            assertEquals(categoryRequest.getCategoryTree(), category.getCategoryTree());

            verify(categoryRepository, times(1)).save(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Category 조회")
    class getCategoryTests {

        @Test
        @DisplayName("get_success_cacheable")
        void getCategory_success() {
            when(categoryRepository.findByUserId(anyLong())).thenReturn(Optional.of(category));

            Category category = categoryService.getCategory(1L);

            assertNotNull(category);
            assertEquals(categoryTree, category.getCategoryTree());

            verify(categoryRepository, times(1)).findByUserId(anyLong());
        }

        @Test
        @DisplayName("get_fail_notFound")
        void getCategory_fail_notFound() {
            when(categoryRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

            assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(1L));

            verify(categoryRepository, times(1)).findByUserId(anyLong());
        }
    }

    @Nested
    @DisplayName("Category 수정")
    class updateCategoryTests {
        @Test
        @DisplayName("update_success")
        void updateCategory_success() {
            when(categoryRepository.findByUserId(anyLong())).thenReturn(Optional.ofNullable(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            Category update = categoryService.updateCategory(categoryRequest.toEntity(), 1L);

            assertNotNull(update);
            assertEquals(categoryTree, update.getCategoryTree());

            verify(categoryRepository, times(1)).findByUserId(anyLong());
            verify(categoryRepository, times(1)).save(any(Category.class));
        }

        @Test
        @DisplayName("update_fail_notFound")
        void updateCategory_fail_notFound() {
            when(categoryRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

            assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(1L));

            verify(categoryRepository, never()).save(any(Category.class));
        }
    }
}
