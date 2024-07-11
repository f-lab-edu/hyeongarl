package com.hyeongarl.controller;

import com.hyeongarl.config.Logger;
import com.hyeongarl.dto.UrlRequestDto;
import com.hyeongarl.dto.UrlResponseDto;
import com.hyeongarl.entity.Url;
import com.hyeongarl.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/url")
public class UrlController {
    private final UrlService urlService;

    /**
     * Url 등록
     * @param urlRequest 입력받은 url 정보
     * @return 저장된 url 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponseDto saveUrl(@RequestBody UrlRequestDto urlRequest) {
        Logger.logging("saveUrl");
        return UrlResponseDto.fromEntity(urlService.save(urlRequest.toEntity()));
    }

    /**
     * Url 단건 조회
     * @param urlId 조회할 Url Id
     * @return 조죄한 url 정보
     */
    @GetMapping("/{urlId}")
    public UrlResponseDto getUrl(@PathVariable Long urlId) {
        Logger.logging("getUrl");
        return UrlResponseDto.fromEntity(urlService.getUrl(urlId));
    }

    /**
     * Url 목록 조회
     * @param page
     * @param size
     * @return Url 목록
     */
    @GetMapping
    public Page<UrlResponseDto> getUrls(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Logger.logging("getUrls");
        Pageable pageable = PageRequest.of(page, size);
        Page<Url> result = urlService.getUrls(pageable);
        return result.map(UrlResponseDto::fromEntity);
    }

    /**
     * UrlAndCategory 수정
     * @param urlId url ID
     * @param urlRequest 수정할 url 정보
     * @return 수정된 UrlAndCategory 정보
     */
    @PutMapping("/{urlId}")
    public UrlResponseDto updateUrl(@PathVariable Long urlId, @RequestBody UrlRequestDto urlRequest) {
        Logger.logging("updateUrl");
        return UrlResponseDto.fromEntity(urlService.updateUrl(urlId, urlRequest));
    }

    /**
     * UrlAndCategory 삭제
     * @param urlId UrlAndCategory ID
     */
    @DeleteMapping("/{urlId}")
    public void deleteUrl(@PathVariable Long urlId){
        Logger.logging("deleteUrl");
        urlService.deleteUrl(urlId);
    }

}
