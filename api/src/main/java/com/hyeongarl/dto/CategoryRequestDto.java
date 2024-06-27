package com.hyeongarl.dto;

import com.hyeongarl.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
    private Map<String, Object> categoryTree;

    public Category toEntity() {
        return Category.builder()
                .categoryTree(categoryTree)
                .build();
    }
}
