package com.hyeongarl.dto;

import com.hyeongarl.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
    @NotBlank(message="CategoryName is required")
    private String categoryName;
    private Long categoryPreId;

    public Category toEntity(){
        return Category.builder()
                .categoryName(categoryName)
                .categoryPreId(categoryPreId)
                .build();
    }
}
