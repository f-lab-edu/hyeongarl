package com.hyeongarl.controller;

import com.hyeongarl.dto.UrlRequestDto;
import com.hyeongarl.dto.UrlResponseDto;
import com.hyeongarl.entity.Url;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/url")
public class UrlController {

    // Sample Data
    private Url url = Url.builder()
            .urlId(10L)
            .url("www.example.com")
            .urlTitle("Test Title")
            .urlDescription("Test Description")
            .categoryId(1L)
            .urlRegDate(null)
            .urlUpdateDate(null)
            .build();

    // Sample Data
    private final List<UrlResponseDto> urls = Arrays.asList(
            new UrlResponseDto(1L, "example.com", "Example Title", "Example Description", 1L, null, null),
            new UrlResponseDto(2L, "sample.com", "Sample Title", "Sample Description", 2L, null, null),
            new UrlResponseDto(3L, "test.com", "Test Title", "Test Description", 3L, null, null)
    );

    /**
     * Url 등록
     * @param urlRequest 입력한 정보
     * @return 생성된 url 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponseDto createUrl(@Valid @RequestBody UrlRequestDto urlRequest) {
        return convertToResponseDTO(urlRequest.toEntity());
    }

    /**
     * Url 목록 조회
     * @return URL 목록
     */
    @GetMapping
    public List<UrlResponseDto> getUrls() {
        return urls;
    }

    /**
     * Url 조회
     * @param urlId url ID
     * @return URL 정보
     */
    @GetMapping("/{urlId}")
    public UrlResponseDto getUrl(@PathVariable Long urlId) {
        return convertToResponseDTO(url);
    }

    /**
     * Url 수정
     * @param urlId url ID
     * @param request 수정할 url 정보
     * @return 수정된 Url 정보
     */
    @PutMapping("/{urlId}")
    public UrlResponseDto updateUrl(@PathVariable Long urlId, @RequestBody UrlRequestDto request) {
        return convertToResponseDTO(request.toEntity());
    }

    /**
     * Url 삭제
     * @param urlId Url ID
     * @return 204 No Content
     */
    @DeleteMapping("/{urlId}")
    public int deleteUrl(@PathVariable Long urlId){
        return -1; //204
    }

    private UrlResponseDto convertToResponseDTO(Url url) {
        return UrlResponseDto.builder()
                .urlId(url.getUrlId())
                .url(url.getUrl())
                .urlTitle(url.getUrlTitle())
                .urlDescription(url.getUrlDescription())
                .categoryId(url.getCategoryId())
                .urlRegDate(url.getUrlRegDate())
                .urlUpdateDate(url.getUrlUpdateDate())
                .build();
    }
}
