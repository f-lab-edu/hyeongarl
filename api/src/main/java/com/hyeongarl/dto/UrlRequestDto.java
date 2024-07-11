package com.hyeongarl.dto;

import com.hyeongarl.entity.Url;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDto {
    @NotNull(message = "URL은 필수 입력값입니다.")
    @URL(message = "유효하지 않은 URL입니다.")
    private String url;

    @NotNull(message = "URL 제목은 필수 입력값입니다.")
    @Size(max = 60, message = "URL 제목은 최대 60자 입니다.")
    private String urlTitle;

    private String urlDescription;

    private Long categoryId;

    public Url toEntity() {
        return Url.builder()
                .url(url)
                .urlTitle(urlTitle)
                .urlDescription(urlDescription)
                .categoryId(categoryId)
                .build();
    }
}
