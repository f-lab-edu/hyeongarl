package com.hyeongarl.dto;

import com.hyeongarl.entity.Url;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlRequestDto {
    @NotNull(message="URL is required")
    private String url;

    @NotNull(message="URL Title is required")
    private String urlTitle;
    private String urlDescription;
    private Long categoryId;

    public Url toEntity(){
        return Url.builder()
                .url(url)
                .urlTitle(urlTitle)
                .urlDescription(urlDescription)
                .categoryId(categoryId)
                .build();
    }
}
