package com.hyeongarl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long categoryId;
    private String categoryName;
    private Long categoryPreId;
    private Long userId;

    private LocalDateTime categoryRegDate;
    private LocalDateTime categoryUpdateDate;
}
