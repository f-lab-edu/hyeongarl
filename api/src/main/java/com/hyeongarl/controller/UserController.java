package com.hyeongarl.controller;

import com.hyeongarl.dto.UserRequestDto;
import com.hyeongarl.dto.UserResponseDto;
import com.hyeongarl.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto saveUser(@Valid @RequestBody UserRequestDto userRequest) {
        return UserResponseDto.fromEntity(userService.save(userRequest.toEntity()));
    }
}
