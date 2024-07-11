package com.hyeongarl.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Table(name="users")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", updatable = false)
    private Long userId;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식에 맞게 작성해주세요.")
    @Column(name="user_email", nullable = false, unique = true)
    private String userEmail;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name="user_regdate", updatable = false)
    private LocalDateTime userRegdate;

    @PrePersist
    protected void onCreate() {
        userRegdate = LocalDateTime.now();
    }
}
