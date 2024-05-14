package com.hyeongarl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyeongarl.dto.CategoryRequestDTO;
import com.hyeongarl.dto.CategoryResponseDTO;
import com.hyeongarl.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryResponseDTO cateResponse;
    private CategoryRequestDTO cateRequest;
    private List<CategoryResponseDTO> categories;

    // TestData
    private static final Long CATE_ID = 1L;
    private static final String CATE_NAME = "Test Name";
    private static final Long CATE_PRE_ID = 2L;
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {  // TestData
        cateRequest = CategoryRequestDTO.builder()
                .categoryName(CATE_NAME)
                .categoryPreId(CATE_PRE_ID)
                .build();

        cateResponse = CategoryResponseDTO.builder()
                .categoryId(CATE_ID)
                .categoryName(CATE_NAME)
                .parentCategory(null)
                .userId(USER_ID)
                .categoryRegDate(null)
                .categoryUpdateDate(null)
                .build();

        categories = Arrays.asList(
                new CategoryResponseDTO(1L, "ExampleName", 1L, null, null, null, null),
                new CategoryResponseDTO(2L, "SampleName", 2L, null, null, null, null),
                new CategoryResponseDTO(3L, "TestName", 3L, null, null, null, null)
        );
    }

    @Test   // Category 등록
    @DisplayName("POST /category : CREATE CATEGORY")
    void createCategory() throws Exception {
        //given
        Category cate = cateRequest.toEntity();
        String cateJson = objectMapper.writeValueAsString(cate);

        //when
        ResultActions action = mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cateJson));

        //then
        action.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test   // Category 목록 조회
    @DisplayName("GET /category/list/{userId} : GET CATEGORIES")
    void getCategories() throws Exception {
        //given
        Long userId = 1L;

        //when
        ResultActions action = mockMvc.perform(get("/category/list/" + userId));

        //then
        action.andExpect(status().isOk())
                .andDo(print());
    }

    @Test   // Category 조회
    @DisplayName("GET /category/{categoryId} : GET CATEGORY")
    void getCategory() throws Exception {
        //given
        Long categoryId = 1L;

        //when
        ResultActions action = mockMvc.perform(get("/category/" + categoryId));

        //then
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(CATE_ID))
                .andDo(print());
    }

    @Test   // Category 수정
    @DisplayName("PUT /category/{categoryId} : PUT CATEGORY")
    void updateCategory() throws Exception {
        //given
        Long categoryId = 1L;
        Category cate = cateRequest.toEntity();
        String cateJson = objectMapper.writeValueAsString(cate);

        //when
        ResultActions action = mockMvc.perform(put("/category/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cateJson));

        //then
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(CATE_ID))
                .andDo(print());
    }

    @Test   // Category 삭제
    @DisplayName("DELETE /category/{categoryId} : DELETE CATEGORY")
    void deleteCategory() throws Exception {
        //given
        Long categoryId = 1L;

        //when
        ResultActions action = mockMvc.perform(delete("/category/" + categoryId));

        //then
        action.andExpect(status().isNoContent())
                .andDo(print());
    }
}
