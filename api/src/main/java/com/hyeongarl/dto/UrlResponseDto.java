package com.hyeongarl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlResponseDto {
    private Long urlId;
    private String url;
    private String urlTitle;
    private String urlDescription;
    private Long categoryId;
    private LocalDateTime urlRegDate;
    private LocalDateTime urlUpdateDate;
}
