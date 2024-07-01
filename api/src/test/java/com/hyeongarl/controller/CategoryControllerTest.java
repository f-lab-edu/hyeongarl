package com.hyeongarl.controller;

import com.hyeongarl.dto.CategoryRequestDto;
import com.hyeongarl.dto.CategoryResponseDto;
import com.hyeongarl.entity.User;
import com.hyeongarl.repository.UserRepository;
import com.hyeongarl.service.TokenService;
import com.hyeongarl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
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
    @DisplayName("testUpdateCategory SyncAsync")
    @Order(5)
    void testUpdateCategorySyncAsync() {
        Map<String, Object> updateTree = createSampleCategoryTree();
        Map<String, Object> addChild = new HashMap<>();
        addChild.put("name", "updateSyncChild");
        updateTree.put("name", addChild);

        CategoryRequestDto updateRequest = CategoryRequestDto.builder()
                .categoryTree(updateTree)
                .build();

        HttpEntity<CategoryRequestDto> updateEntity = new HttpEntity<>(updateRequest, headers);

        log.info("start Sync >>>>> ");
        long startTime = System.currentTimeMillis();
        restTemplate.exchange("http://localhost:" + port + "/category/sync", HttpMethod.PUT, updateEntity, CategoryResponseDto.class);
        long endTime = System.currentTimeMillis();
        log.info("                  >>>>>  Sync Time : {}", (endTime - startTime));

        System.out.println("start Async >>>>> ");
        long startTimeAsync = System.currentTimeMillis();
        restTemplate.exchange("http://localhost:" + port + "/category/async", HttpMethod.PUT, updateEntity, CategoryResponseDto.class);
        long endTimeAsync = System.currentTimeMillis();
        log.info("                  >>>>> Async Time : {}", (endTimeAsync - startTimeAsync));

        log.info("Sync : {}", (endTime - startTime));
        log.info("Async : {}", (endTimeAsync - startTimeAsync));
    }

    @Test
    @DisplayName("testUpdateCategory Sync")
    @Order(3)
    void testUpdateCategorySync() {
        String url = "http://localhost:" + port + "/category/sync";
        Map<String, Object> updateTree = createSampleCategoryTree();
        Map<String, Object> addChild = new HashMap<>();
        addChild.put("name", "updateSyncChild");
        updateTree.put("name", addChild);

        CategoryRequestDto updateRequest = CategoryRequestDto.builder()
                .categoryTree(updateTree)
                .build();

        HttpEntity<CategoryRequestDto> updateEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<CategoryResponseDto> responseEntity
                = restTemplate.exchange(url, HttpMethod.PUT, updateEntity, CategoryResponseDto.class);

        assertThat(responseEntity).isNotNull();

        CategoryResponseDto responseBody = responseEntity.getBody();
    }

    @Test
    @DisplayName("testUpdateCategory Async")
    @Order(4)
    void testUpdateCategoryAsync() {
        String url = "http://localhost:" + port + "/category/async";
        Map<String, Object> updateTree = createSampleCategoryTree();
        Map<String, Object> addChild = new HashMap<>();
        addChild.put("name", "updateAsyncChild");
        updateTree.put("name", addChild);

        CategoryRequestDto updateRequest = CategoryRequestDto.builder()
                .categoryTree(updateTree)
                .build();

        HttpEntity<CategoryRequestDto> updateEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<CategoryResponseDto> responseEntity
                = restTemplate.exchange(url, HttpMethod.PUT, updateEntity, CategoryResponseDto.class);

        assertThat(responseEntity).isNotNull();

        CategoryResponseDto responseBody = responseEntity.getBody();
    }

    @Test
    @DisplayName("testDeleteCategory")
    @Order(6)
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
