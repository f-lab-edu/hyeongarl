package com.hyeongarl.controller;

import com.hyeongarl.dto.CategoryRequestDto;
import com.hyeongarl.dto.CategoryResponseDto;
import com.hyeongarl.service.CategoryService;
import com.hyeongarl.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final TokenService tokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto saveCategory(@RequestBody CategoryRequestDto categoryRequest) {
        return CategoryResponseDto.fromEntity(categoryService.save(categoryRequest.toEntity(), tokenService.getUserId()));
    }

    @GetMapping
    public CategoryResponseDto getCategory() {
        return CategoryResponseDto.fromEntity(categoryService.getCategory(tokenService.getUserId()));
    }

    @PutMapping // CompletableFuture 적용
    public CompletableFuture<CategoryResponseDto> updateCategory(@RequestBody CategoryRequestDto categoryRequest) {
        return categoryService.updateCategory(categoryRequest.toEntity(), tokenService.getUserId())
                .thenApply(CategoryResponseDto::fromEntity);
    }

    /** Async 비교 **/
    @PutMapping("/async")
    public CategoryResponseDto updateCategoryAsync(@RequestBody CategoryRequestDto categoryRequest) throws ExecutionException, InterruptedException {
        CompletableFuture<CategoryResponseDto> update = categoryService.updateCategoryAsync(categoryRequest.toEntity(), tokenService.getUserId())
                .thenApply(CategoryResponseDto::fromEntity);
        return update.get();
    }

    /** Sync 비교 **/
    @PutMapping("/sync")
    public CategoryResponseDto updateCategorySync(@RequestBody CategoryRequestDto categoryRequest) {
        return CategoryResponseDto.fromEntity(categoryService.updateCategorySync(categoryRequest.toEntity(), tokenService.getUserId()));
    }


    // 사용자 탈퇴시 전체 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId, tokenService.getUserId());
        return ResponseEntity.ok("삭제완료");
    }
}
