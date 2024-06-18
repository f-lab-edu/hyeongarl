package com.hyeongarl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hyeongarl.entity.Url;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponseDto {
    private Long urlId;
    private String url;
    private String urlTitle;
    private String urlDescription;
    private Long categoryId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime urlRegdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime urlUpdate;

    public static UrlResponseDto fromEntity(Url url) {
        return UrlResponseDto.builder()
                .urlId(url.getUrlId())
                .url(url.getUrl())
                .urlTitle(url.getUrlTitle())
                .urlDescription(url.getUrlDescription() == null ? null : url.getUrlDescription())
                .categoryId(url.getCategoryId() == null ? null : url.getCategoryId())
                .urlRegdate(url.getUrlRegdate() == null ? null : url.getUrlRegdate())
                .urlUpdate(url.getUrlUpdate() == null ? null : url.getUrlUpdate())
                .build();
    }
}
