package com.hyeongarl.controller;

import com.hyeongarl.dto.CategoryRequestDTO;
import com.hyeongarl.dto.CategoryResponseDTO;
import com.hyeongarl.entity.Category;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    // Sample Data
    private Category cate = Category.builder()
            .categoryId(1L)
            .categoryName("Test Category")
            .categoryPreId(10L)
            .userId(1L)
            .categoryRegDate(null)
            .categoryUpdateDate(null)
            .build();

    /**
     * Category 등록
     * @param categoryRequest 등록할 카테고리 정보
     * @return 등록한 카테고리 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequest) {
        CategoryResponseDTO response = convertToResponseDTO(categoryRequest.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Category 목록 조회
     * @param userId 사용자 ID
     * @return 사용자 카테고리 목록
     */
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<CategoryResponseDTO>> getCategories(@PathVariable Long userId){
        List<CategoryResponseDTO> result = Arrays.asList(
                new CategoryResponseDTO(1L, "ExampleName", 1L, null, null, null, null),
                new CategoryResponseDTO(2L, "SampleName", 2L, null, null, null, null),
                new CategoryResponseDTO(3L, "TestName", 3L, null, null, null, null)
        );

        return ResponseEntity.ok(result);
    }

    /**
     * Category 조회
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(convertToResponseDTO(cate));
    }

    /**
     * Category 수정
     * @param categoryId 카테고리 ID
     * @param categoryRequest 수정할 카테고리 정보
     * @return 수정된 카테고리 정보
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequestDTO categoryRequest) {
        return ResponseEntity.ok(convertToResponseDTO(cate));
    }

    /**
     * Category 삭제
     * @param categoryId 카테고리 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> delteCategory(@PathVariable Long categoryId) {
        return ResponseEntity.noContent().build();
    }

    private CategoryResponseDTO convertToResponseDTO(Category cate) {
        return CategoryResponseDTO.builder()
                .categoryId(cate.getCategoryId())
                .categoryName(cate.getCategoryName())
                .parentCategory(null)
                .userId(cate.getUserId())
                .categoryRegDate(cate.getCategoryRegDate())
                .categoryUpdateDate(cate.getCategoryUpdateDate())
                .build();
    }
}
