package com.hyeongarl.controller;

import com.hyeongarl.dto.UserRequestDto;
import com.hyeongarl.dto.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("login_success")
    void testLogin_success() {
        UserRequestDto userRequest
                = new UserRequestDto("login@email.com", "loginPassword");

        ResponseEntity<UserResponseDto> saveUser
                = restTemplate.postForEntity("/user", userRequest, UserResponseDto.class);
        ResponseEntity<String> responseEntity
                = restTemplate.postForEntity("/login", userRequest, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }
}
