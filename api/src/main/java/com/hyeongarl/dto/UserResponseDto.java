package com.hyeongarl.dto;

import com.hyeongarl.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String userEmail;
    private String password;
    private LocalDateTime userRegdate;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .userEmail(user.getUserEmail())
                .password(user.getPassword())
                .userRegdate(user.getUserRegdate())
                .build();
    }
}
