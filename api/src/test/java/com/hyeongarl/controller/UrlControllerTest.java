package com.hyeongarl.controller;

import com.hyeongarl.dto.UrlRequestDto;
import com.hyeongarl.dto.UrlResponseDto;
import com.hyeongarl.entity.User;
import com.hyeongarl.service.TokenService;
import com.hyeongarl.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    HttpHeaders headers;
    UrlRequestDto urlRequest;
    HttpEntity<UrlRequestDto> requestEntity;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        User testUser = User.builder()
                .userId(1L)
                .userEmail("testUser@email.com")
                .password("testUserPassword")
                .userRegdate(LocalDateTime.now())
                .build();

        userService.save(testUser);
        String token = tokenService.login(testUser);
        headers = new HttpHeaders();
        headers.set("token", token);

        urlRequest = UrlRequestDto.builder()
                .url("https://www.naver.com")
                .urlTitle("testUrlTtitle")
                .urlDescription("testUrlDescription")
                .categoryId(1L)
                .build();

        requestEntity = new HttpEntity<>(urlRequest, headers);
    }

    @Test
    @DisplayName("testSaveUrl")
    void testSaveUrl() {
        ResponseEntity<UrlResponseDto> responseEntity
                = restTemplate.exchange("/url", HttpMethod.POST, requestEntity, UrlResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        UrlResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUrl()).isEqualTo("https://www.naver.com");
    }

    @Test
    @DisplayName("testGetUrl")
    void testGetUrl() {
        ResponseEntity<UrlResponseDto> saveEntity
                = restTemplate.exchange("/url", HttpMethod.POST, requestEntity, UrlResponseDto.class);

        ResponseEntity<UrlResponseDto> responseEntity
                = restTemplate.exchange("/url/1", HttpMethod.GET, requestEntity, UrlResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UrlResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUrlId()).isEqualTo(1L);
    }

//    @Test   // GET /url 목록 조회
//    public void testGetUrls() {
//        int page = 0;
//        int size = 10;
//        ResponseEntity<UrlResponseDto> saveEntity
//                = restTemplate.exchange("/url", HttpMethod.POST, requestEntity, UrlResponseDto.class);
//
//        HttpEntity<String> getRequest = new HttpEntity<>(null, headers);
//
//        ResponseEntity<Page<UrlResponseDto>> response
//                = restTemplate.exchange(
//                        "/url",
//                        HttpMethod.GET,
//                        getRequest,
//                        new ParameterizedTypeReference<Page<UrlResponseDto>>() {}
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        Page<UrlResponseDto> urls = response.getBody();
//        assertThat(urls).isNotNull();
//        assertThat(urls.getContent().size()).isGreaterThan(0);
//    }

    @Test   // PUT /url/1 수정
    public void testUpdateUrl() {
        ResponseEntity<UrlResponseDto> saveEntity
                = restTemplate.exchange("/url", HttpMethod.POST, requestEntity, UrlResponseDto.class);

        ResponseEntity<UrlResponseDto> responseEntity
                = restTemplate.exchange("/url/1", HttpMethod.PUT, requestEntity, UrlResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UrlResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUrl()).isEqualTo("https://www.naver.com");
        assertThat(responseBody.getUrlTitle()).isEqualTo("testUrlTtitle");
        assertThat(responseBody.getUrlDescription()).isEqualTo("testUrlDescription");
    }

    @Test   // DELETE /url/1 삭제 요청
    public void testDeleteUrl() {
        ResponseEntity<UrlResponseDto> saveEntity
                = restTemplate.exchange("/url", HttpMethod.POST, requestEntity, UrlResponseDto.class);

        ResponseEntity<Void> responseEntity
                = restTemplate.exchange("/url/1", HttpMethod.DELETE, requestEntity, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
