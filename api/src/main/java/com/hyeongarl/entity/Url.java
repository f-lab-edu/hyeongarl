package com.hyeongarl.entity;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    private Long urlId;
    private String url;
    private String urlTitle;
    private String urlDescription;
    private Long categoryId;
    private Long userId;
    private LocalDateTime urlRegDate;
    private LocalDateTime urlUpdateDate;
}
