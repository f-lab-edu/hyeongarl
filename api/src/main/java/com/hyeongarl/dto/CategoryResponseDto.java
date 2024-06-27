package com.hyeongarl.dto;

import com.hyeongarl.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Long categoryId;
    private Map<String, Object> categoryTree;

    public static CategoryResponseDto fromEntity(Category category) {
        return CategoryResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryTree(category.getCategoryTree())
                .build();
    }
}
