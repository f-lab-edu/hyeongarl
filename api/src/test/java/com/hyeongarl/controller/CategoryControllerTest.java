package com.hyeongarl.controller;

import com.hyeongarl.dto.CategoryRequestDto;
import com.hyeongarl.dto.CategoryResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test // POST /category 등록
    public void testCreateCategory() {
        CategoryRequestDto categoryRequest = CategoryRequestDto.builder()
                .categoryName("create Name")
                .categoryPreId(1L)
                .build();

        ResponseEntity<CategoryResponseDto> responseEntity
                = restTemplate.postForEntity("/category", categoryRequest, CategoryResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        CategoryResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getCategoryName()).isEqualTo("create Name");
    }

    @Test   // GET /category 목록 조회
    public void testGetCategories() {
        ResponseEntity<List<CategoryResponseDto>> responseEntity
                = restTemplate.exchange("/category", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CategoryResponseDto>>() {});

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<CategoryResponseDto> responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(3);
    }

    @Test   // GET /category/1
    public void testGetCategory() {
        ResponseEntity<CategoryResponseDto> responseEntity = restTemplate.getForEntity("/category/1", CategoryResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        CategoryResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getCategoryId()).isEqualTo(1L);
    }

    @Test   // PUT /category/1
    public void testUpdateCategory() {
        CategoryRequestDto request = new CategoryRequestDto("updateName", 2L);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CategoryRequestDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<CategoryResponseDto> responseEntity
                = restTemplate.exchange("/category/2", HttpMethod.PUT, entity, CategoryResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        CategoryResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
    }

    @Test
    public void testDeleteCategory() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/category/1", HttpMethod.DELETE, entity, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
