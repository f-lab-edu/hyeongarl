package com.hyeongarl.service;

import com.hyeongarl.config.Logger;
import com.hyeongarl.dto.UrlRequestDto;
import com.hyeongarl.entity.Url;
import com.hyeongarl.error.UrlAlreadyExistException;
import com.hyeongarl.error.UrlInvalidException;
import com.hyeongarl.error.UrlNotFoundException;
import com.hyeongarl.repository.UrlRepository;
import com.hyeongarl.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final TokenService tokenService;

    /**
     * Url 등록
     * @param url 입력한 Url 정보
     * @return 저장된 url 정보
     */
    public Url save(Url url) {
        Logger.servicelogging("save");
        url.setUserId(tokenService.getUserId());

        // url 유효성 검사
        if(!UrlValidator.checkUrl(url.getUrl())) {
            throw new UrlInvalidException();
        }

        // 이미 존재하는 정보(사용자, url())
        if(urlRepository.existsByUrl(url.getUrl(), url.getUserId())) {
            throw new UrlAlreadyExistException();
        }
        return urlRepository.save(url);
    }

    /**
     * Url 단건 조회
     * @param urlId 조회할 Url Id
     * @return 조회한 url 정보
     */
    public Url getUrl(Long urlId) {
        Logger.servicelogging("getUrl");
        return urlRepository.findByUrlIdAndUserId(urlId, tokenService.getUserId())
                .orElseThrow(UrlNotFoundException::new);
    }

    /**
     * Url 목록 조회
     * @param pageable 조회할 정보
     * @return 조회한 Url 목록
     */
    public Page<Url> getUrls(Pageable pageable) {
        Logger.servicelogging("getUrls");
        return urlRepository.findAllByUserId(pageable, tokenService.getUserId());
    }

    /**
     * Url 정보 수정
     * @param urlId 수정할 url Id
     * @param urlRequest 수정한 url 정보
     * @return 수정된 url 정보
     */
    public Url updateUrl(Long urlId, UrlRequestDto urlRequest) {
        Logger.servicelogging("updateUrl");
        Url url = getUrl(urlId);

        // url 중복인 경우
        if(urlRepository.existsByUrl(urlRequest.getUrl(), url.getUserId())) {
            throw new UrlAlreadyExistException();
        }

        url.setUrl(urlRequest.getUrl() != null ?
                urlRequest.getUrl() : url.getUrl());
        url.setUrlTitle(urlRequest.getUrlTitle() != null ?
                urlRequest.getUrlTitle() : url.getUrlTitle());
        url.setUrlDescription(urlRequest.getUrlDescription() != null ?
                urlRequest.getUrlDescription() : url.getUrlDescription());
        url.setCategoryId(urlRequest.getCategoryId() != null ?
                urlRequest.getCategoryId() : url.getCategoryId());
        url.setUrlUpdate(LocalDateTime.now());

        return urlRepository.save(url);
    }

    /**
     * Url 삭제
     * @param urlId 삭제할 url Id
     */
    public void deleteUrl(Long urlId) {
        Logger.servicelogging("deleteUrl");
        urlRepository.deleteById(urlId);
    }
}
