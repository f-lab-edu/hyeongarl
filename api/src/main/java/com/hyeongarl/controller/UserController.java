package com.hyeongarl.controller;

import com.hyeongarl.config.Logger;
import com.hyeongarl.dto.UserRequestDto;
import com.hyeongarl.dto.UserResponseDto;
import com.hyeongarl.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addUser(@Valid @RequestBody UserRequestDto userRequest) {
        Logger.logging("addUser");
        return UserResponseDto.fromEntity(userService.save(userRequest.toEntity()));
    }
}
