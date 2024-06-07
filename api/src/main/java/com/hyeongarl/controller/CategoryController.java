package com.hyeongarl.controller;

import com.hyeongarl.dto.CategoryRequestDto;
import com.hyeongarl.dto.CategoryResponseDto;
import com.hyeongarl.entity.Category;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    // Sample Data
    private Category cate = Category.builder()
            .categoryId(1L)
            .categoryName("create Name")
            .categoryPreId(10L)
            .userId(1L)
            .categoryRegDate(null)
            .categoryUpdateDate(null)
            .build();

    // Sample Data
    List<CategoryResponseDto> categories = Arrays.asList(
            new CategoryResponseDto(1L, "ExampleName", 1L, null, null, null, null),
            new CategoryResponseDto(2L, "SampleName", 2L, null, null, null, null),
            new CategoryResponseDto(3L, "TestName", 3L, null, null, null, null)
    );

    /**
     * Category 등록
     * @param categoryRequest 등록할 카테고리 정보
     * @return 등록한 카테고리 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto categoryRequest) {
        CategoryResponseDto response = convertToResponseDTO(categoryRequest.toEntity());
        return response;
    }

    /**
     * Category 목록 조회
     * @return 사용자 카테고리 목록
     */
    @GetMapping
    public List<CategoryResponseDto> getCategories(){
        return categories;
    }

    /**
     * Category 조회
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    @GetMapping("/{categoryId}")
    public CategoryResponseDto getCategory(@PathVariable Long categoryId) {
        return convertToResponseDTO(cate);
    }

    /**
     * Category 수정
     * @param categoryId 카테고리 ID
     * @param categoryRequest 수정할 카테고리 정보
     * @return 수정된 카테고리 정보
     */
    @PutMapping("/{categoryId}")
    public CategoryResponseDto updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequestDto categoryRequest) {
        return convertToResponseDTO(cate);
    }

    /**
     * Category 삭제
     * @param categoryId 카테고리 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{categoryId}")
    public int deleteCategory(@PathVariable Long categoryId) {
        return 1;
    }

    private CategoryResponseDto convertToResponseDTO(Category cate) {
        return CategoryResponseDto.builder()
                .categoryId(cate.getCategoryId())
                .categoryName(cate.getCategoryName())
                .categoryName(cate.getCategoryName())
                .build();
    }
}
