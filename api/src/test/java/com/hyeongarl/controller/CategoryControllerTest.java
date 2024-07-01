package com.hyeongarl.controller;

import com.hyeongarl.dto.CategoryRequestDto;
import com.hyeongarl.dto.CategoryResponseDto;
import com.hyeongarl.entity.User;
import com.hyeongarl.repository.UserRepository;
import com.hyeongarl.service.TokenService;
import com.hyeongarl.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryControllerTest {

    HttpHeaders headers;

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpEntity<CategoryRequestDto> requestEntity;

    private CategoryResponseDto categoryResponse;

    @LocalServerPort
    private int port;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User testUser = User.builder()
                .userId(1L)
                .userEmail("testUser44@email.com")
                .password("testUserPassword")
                .userRegdate(LocalDateTime.now())
                .build();

        if(userRepository.findByUserEmail(testUser.getUserEmail()).isEmpty()) {
            userService.save(testUser);
        }
        String token = tokenService.login(testUser);
        headers = new HttpHeaders();
        headers.set("token", token);

        Map<String, Object> categoryTree = createSampleCategoryTree();

        CategoryRequestDto categoryRequest = CategoryRequestDto.builder()
                .categoryTree(categoryTree)
                .build();

        categoryResponse = CategoryResponseDto.builder()
                .categoryId(1L)
                .categoryTree(categoryTree)
                .build();

        requestEntity = new HttpEntity<>(categoryRequest, headers);
    }

    @Test
    @DisplayName("testSaveCategory")
    @Order(1)
    void testSaveCategory() {
        ResponseEntity<CategoryResponseDto> responseEntity
                = restTemplate.exchange("/category", HttpMethod.POST, requestEntity, CategoryResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        CategoryResponseDto responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
    }

    @Test
    @DisplayName("testGetCategory")
    @Order(2)
    void testGetCategory() {
        ResponseEntity<CategoryResponseDto> responseEntity
                = restTemplate.exchange("/category", HttpMethod.GET, requestEntity, CategoryResponseDto.class);

        assertThat(responseEntity).isNotNull();

        CategoryResponseDto responseBody = responseEntity.getBody();
        if (responseBody != null) {
            assertEquals(categoryResponse.getCategoryTree(), responseBody.getCategoryTree());
        }
    }

    @Test
    @DisplayName("testUpdateCategory")
    @Order(3)
    void testUpdateCategory() {
        String url = "http://localhost:" + port + "/category";
        Map<String, Object> updateTree = createSampleCategoryTree();
        CategoryRequestDto updateRequest = CategoryRequestDto.builder()
                .categoryTree(updateTree)
                .build();

        HttpEntity<CategoryRequestDto> updateEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<CategoryResponseDto> responseEntity
                = restTemplate.exchange(url, HttpMethod.PUT, updateEntity, CategoryResponseDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity).isNotNull();

        CategoryResponseDto responseBody = responseEntity.getBody();
        assertEquals(updateTree, Objects.requireNonNull(responseBody).getCategoryTree());
    }

    @Test
    @DisplayName("testDeleteCategory")
    @Order(4)
    void testDeleteCategory() {
        String url = "http://localhost:" + port + "/category/1";

        requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

        assertEquals("삭제완료", responseEntity.getBody());
    }

    private Map<String, Object> createSampleCategoryTree() {
        Map<String, Object> rootNode = new HashMap<>();
        rootNode.put("name", "root");

        Map<String, Object> childNode1 = new HashMap<>();
        childNode1.put("name", "child1");

        Map<String, Object> childNode2 = new HashMap<>();
        childNode2.put("name", "child2");

        List<Map<String, Object>> children = new ArrayList<>();
        children.add(childNode1);
        children.add(childNode2);

        rootNode.put("children", children);

        return rootNode;
    }
}
