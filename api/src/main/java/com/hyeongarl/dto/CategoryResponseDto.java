package com.hyeongarl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private Long userId;
    
    private CategoryResponseDto parentCategory; // 상위 카테고리 정보 (최상위까지 재귀적 참조)
    private List<CategoryResponseDto> subCategories;
    
    private LocalDateTime categoryRegDate;
    private LocalDateTime categoryUpdateDate;
}
