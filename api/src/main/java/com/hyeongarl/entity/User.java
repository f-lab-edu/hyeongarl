package com.hyeongarl.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/*
    @Table(name="")
        데이터베이스의 name에 지정된 이름의 테이블과 매핑된다.
    @Entity
        JPA 엔티티임을 명시하여 데이터베이스 테이블의 레코드와 매핑
    @Getter, @Setter
        롬복 라이브러리의 어노테이션으로 모든 필드에 대해 자동으로 getter/setter를 생성
    @NoArgsConstructor
        롬복 라이브러리의 어노테이션으로 매개변수가 없는 기본 생성자를 자동으로 생성
 */
@Table(name="users")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /*
        @Id
            테이블의 기본 키
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            기본 피의 생성 전략을 지정
            IDENTITY : 데이터베이스가 기본 키 생성을 담당
            기본적으로 AUTO_INCREMENT를 사용
        @Column(name="user_id", updatable = false)
            데이터베이스의 name과 일치하는 컬럼과 매핑된다.
            필드가 수정될 수 없음을 의미
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", updatable = false)
    private Long userId;

    /*
        @Email
            유효한 이메일 형식을 규정
        @Column(name="user_email", nullable = false, unique = true)
            데이터베이스의 name과 일치하는 컬럼과 매핑된다.
            null 값을 가질 수 없음을 의미
            유일한 값을 가져야 함을 의미
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식에 맞게 작성해주세요.")
    @Column(name="user_email", nullable = false, unique = true)
    private String userEmail;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Column(name = "user_password", nullable = false)
    // @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])^[a-zA-Z0-9~!@#$%^&*()+|=]{8,15}$", message = "비밀번호는 최소 8자 이상, 15자 이하이며, 영문과 숫자, 특수문자만 입력하세요.")
    private String password;

    @Column(name="user_regdate", updatable = false)
    private LocalDateTime userRegdate;

    @PrePersist
    protected void onCreate() {
        userRegdate = LocalDateTime.now();
    }
}
