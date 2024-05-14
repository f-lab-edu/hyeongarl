package com.hyeongarl.controller;

import com.hyeongarl.dto.UrlRequestDTO;
import com.hyeongarl.dto.UrlResponseDTO;
import com.hyeongarl.entity.Url;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Url 등록
     * @param urlRequest
     * @return 생성된 url 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UrlResponseDTO> createUrl(@Valid @RequestBody UrlRequestDTO urlRequest) {
        // Url url = urlService.createUrl(urlRequest.toEntity());
        UrlResponseDTO response = convertToResponseDTO(urlRequest.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Url 목록 조회
     * @param userId 사용자 ID
     * @return URL 목록
     */
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<UrlResponseDTO>> getUrls(@PathVariable Long userId) {
        List<UrlResponseDTO> result = Arrays.asList(
                new UrlResponseDTO(1L, "example.com", "Example Title", "Example Description", 1L, null, null),
                new UrlResponseDTO(2L, "sample.com", "Sample Title", "Sample Description", 2L, null, null),
                new UrlResponseDTO(3L, "test.com", "Test Title", "Test Description", 3L, null, null)
        );

        return ResponseEntity.ok(result);
    }

    /**
     * Url 조회
     * @param urlId url ID
     * @return URL 정보
     */
    @GetMapping("/{urlId}")
    public ResponseEntity<UrlResponseDTO> getUrl(@PathVariable Long urlId) {
        //Url url = urlService.getUrlById(urlId);

        if(url == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToResponseDTO(url));
    }

    /**
     * Url 수정
     * @param urlId url ID
     * @param request 수정할 url 정보
     * @return 수정된 Url 정보
     */
    @PutMapping("/{urlId}")
    public ResponseEntity<UrlResponseDTO> updateUrl(@PathVariable Long urlId, @RequestBody UrlRequestDTO request) {
        //Url url = urlService.updateUrl(urlId, request.toEntity());

        if(url == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToResponseDTO(url));
    }

    /**
     * Url 삭제
     * @param urlId Url ID
     * @return 204 No Content
     */
    @DeleteMapping("/{urlId}")
    public ResponseEntity<?> deleteUrl(@PathVariable Long urlId){
        //Boolean isDeleted = urlService.deleteUrl(urlId);
        Boolean isDeleted = true;

        if(!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build(); //204
    }

    private UrlResponseDTO convertToResponseDTO(Url url) {
        return UrlResponseDTO.builder()
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
