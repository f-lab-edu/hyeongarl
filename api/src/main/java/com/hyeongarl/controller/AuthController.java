package com.hyeongarl.controller;

import com.hyeongarl.config.Logger;
import com.hyeongarl.dto.UserRequestDto;
import com.hyeongarl.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto userRequest) {
        Logger.logging("login");
        return ResponseEntity.ok().header("token",
                        tokenService.login(userRequest.toEntity()))
                .body("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Logger.logging("logout");
        return ResponseEntity.ok("로그아웃이 성공적으로 처리되었습니다.");
    }
}
