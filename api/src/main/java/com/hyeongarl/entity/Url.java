package com.hyeongarl.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Table(name="urls")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlId;

    @URL
    @Column(name="url", nullable = false)
    private String url;

    @Column(name="url_title", length = 60, nullable = false)
    private String urlTitle;

    @Column(name = "url_description", length = 150)
    private String urlDescription;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "url_regdate")
    private LocalDateTime urlRegdate;

    @Column(name = "url_update")
    private LocalDateTime urlUpdate;

    @PrePersist // 등록 시 자동으로 실행
    public void prePersist() {
        this.urlRegdate = LocalDateTime.now();
    }

    @PreUpdate // 업데이트 시 자동으로 실행
    public void preUpdate() {
        this.urlUpdate = LocalDateTime.now();
    }
}
