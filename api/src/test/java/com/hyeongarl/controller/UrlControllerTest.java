package com.hyeongarl.controller;

import com.hyeongarl.dto.UrlRequestDto;
import com.hyeongarl.dto.UrlResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    // (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 있어야 autowired 가능

    @Test   // POST /url 등록
    public void testCreateUrl() {
        UrlRequestDto request
                = new UrlRequestDto("create.com", "create Title", "create Description", 1L);

        ResponseEntity<UrlResponseDto> responseEntity
                = restTemplate.postForEntity("/url", request, UrlResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        UrlResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUrl()).isEqualTo("create.com");
        assertThat(responseBody.getUrlTitle()).isEqualTo("create Title");
        assertThat(responseBody.getUrlDescription()).isEqualTo("create Description");
    }

    @Test   // GET /url 목록 조회
    public void testGetUrls() {
        ResponseEntity<List<UrlResponseDto>> responseEntity = restTemplate.exchange(
                "/url", HttpMethod.GET, null, new ParameterizedTypeReference<List<UrlResponseDto>>() {}
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<UrlResponseDto> responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(3);
    }

    @Test   // GET url/1 상세 조회
    public void testGetUrl() {
        ResponseEntity<UrlResponseDto> responseEntity = restTemplate.getForEntity("/url/10", UrlResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UrlResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUrlId()).isEqualTo(10L);
    }

    @Test   // PUT /url/2 수정
    public void testUpdateUrl() {
        UrlRequestDto request
                = new UrlRequestDto("update.com", "update Title", "update Description", 2L);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UrlRequestDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<UrlResponseDto> responseEntity
                = restTemplate.exchange("/url/2", HttpMethod.PUT, entity, UrlResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UrlResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUrl()).isEqualTo("update.com");
        assertThat(responseBody.getUrlTitle()).isEqualTo("update Title");
        assertThat(responseBody.getUrlDescription()).isEqualTo("update Description");
    }

    @Test   // DELETE /url/1 삭제 요청
    public void testDeleteUrl() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/url/1", HttpMethod.DELETE, entity, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
