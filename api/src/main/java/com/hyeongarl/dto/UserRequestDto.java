package com.hyeongarl.dto;

import com.hyeongarl.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String userEmail;
    private String password;

    public User toEntity() {
        return User.builder()
                .userEmail(userEmail)
                .password(password)
                .build();
    }
}
