package com.hyeongarl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyeongarl.dto.UrlRequestDTO;
import com.hyeongarl.dto.UrlResponseDTO;
import com.hyeongarl.entity.Url;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UrlRequestDTO urlRequest;
    private UrlResponseDTO urlResponse;
    private List<UrlResponseDTO> urlResponseList;

    // TestData
    private static final Long URL_ID=10L;
    private static final String URL_NAME="www.example.com";
    private static final String URL_TITLE="Test Title";
    private static final String URL_DESCRIPTION="Test Description";
    private static final Long CATEGORY_ID=1L;

    @BeforeEach
    void setUp() {  // TestData
        urlRequest = UrlRequestDTO.builder()
                .url(URL_NAME)
                .urlTitle(URL_TITLE)
                .urlDescription(URL_DESCRIPTION)
                .categoryId(CATEGORY_ID)
                .build();

        urlResponse = UrlResponseDTO.builder()
                .urlId(URL_ID)
                .url(URL_NAME)
                .urlTitle(URL_TITLE)
                .urlDescription(URL_DESCRIPTION)
                .categoryId(CATEGORY_ID)
                .urlRegDate(null)
                .urlUpdateDate(null)
                .build();

        urlResponseList = Arrays.asList(
                new UrlResponseDTO(1L, "example.com", "Example Title", "Example Description", 1L, null, null),
                new UrlResponseDTO(2L, "sample.com", "Sample Title", "Sample Description", 2L, null, null),
                new UrlResponseDTO(3L, "test.com", "Test Title", "Test Description", 3L, null, null)
        );
    }

    @Test   // URL 등록
    @DisplayName("POST /url : CREATE URL")
    void createURL() throws Exception {
        //given
        Url url = urlRequest.toEntity();
        String urlJson = objectMapper.writeValueAsString(url);

        //when
        ResultActions action = mockMvc.perform(post("/url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(urlJson));

        //then
        action.andExpect(status().isCreated())
                //.andExpect(content().string(containsString("Test Title")))
                .andDo(print());
    }

    @Test   // URL 목록 조회
    @DisplayName("GET /url/list/{userId} : GET URLS")
    void getUrls() throws Exception {
        //given
        Long userId = 1L;

        //when
        ResultActions action = mockMvc.perform(get("/url/"+userId));

        //then
        action.andExpect(status().isOk())
                .andDo(print());
    }

    @Test   // URL 조회
    @DisplayName("GET /url/{urlId} : GET URL")
    void getUrl() throws Exception {
        //given
        Long urlId = 10L;

        //when
        ResultActions action = mockMvc.perform(get("/url/" + urlId));

        //then
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(URL_NAME))
                .andDo(print());
    }

    @Test   // URL 수정
    @DisplayName("PUT /url/{urlId} : UPDATE URL")
    void updateUrl() throws Exception {
        //given
        Long urlId = 10L;

        //when
        ResultActions action = mockMvc.perform(put("/url/" + urlId)
                .content(objectMapper.writeValueAsString(urlRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.urlId").value(URL_ID))
                .andDo(print());
    }

    @Test   // URL 삭제
    @DisplayName("DELETE /url/{urlId} :DELETE URL")
    void deleteUrl() throws Exception{
        //given
        Long urlId = 1L;

        //when
        ResultActions action = mockMvc.perform(delete("/url/" + urlId));

        //then
        action.andExpect(status().isNoContent())
                .andDo(print());
    }
}
