package com.hyeongarl.service;

import com.hyeongarl.dto.UrlRequestDto;
import com.hyeongarl.entity.Url;
import com.hyeongarl.error.UrlAlreadyExistException;
import com.hyeongarl.error.UrlInvalidException;
import com.hyeongarl.error.UrlNotFoundException;
import com.hyeongarl.repository.UrlRepository;
import com.hyeongarl.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final TokenService tokenService;
    @Qualifier("urlTemplate")
    private final KafkaTemplate<String, Object> urlTemplate;

    /**
     * Url 등록
     * @param url 입력한 Url 정보
     * @return 저장된 url 정보
     */
    public Url save(Url url) {
        url.setUserId(tokenService.getUserId());

        // url 유효성 검사
        if(!UrlValidator.checkUrl(url.getUrl())) {
            throw new UrlInvalidException();
        }

        // 이미 존재하는 정보(사용자, url())
        if(urlRepository.existsByUrl(url.getUrl(), url.getUserId())) {
            throw new UrlAlreadyExistException();
        }

        Url savedUrl = urlRepository.save(url);
        sendMessage("saveUrl", savedUrl);
        return savedUrl;
    }

    /**
     * Url 단건 조회
     * @param urlId 조회할 Url Id
     * @return 조회한 url 정보
     */
    public Url getUrl(Long urlId) {
        return urlRepository.findByUrlIdAndUserId(urlId, tokenService.getUserId())
                .orElseThrow(UrlNotFoundException::new);
    }

    /**
     * Url 목록 조회
     * @param pageable 조회할 정보
     * @return 조회한 Url 목록
     */
    public Page<Url> getUrls(Pageable pageable) {
        return urlRepository.findAllByUserId(pageable, tokenService.getUserId());
    }

    /**
     * Url 정보 수정
     * @param urlId 수정할 url Id
     * @param urlRequest 수정한 url 정보
     * @return 수정된 url 정보
     */
    public Url updateUrl(Long urlId, UrlRequestDto urlRequest) {
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
        urlRepository.deleteById(urlId);
    }

    void sendMessage(String topic, Url url) {
        urlTemplate.send(topic, url);
    }

    @KafkaListener(topics = "saveUrl", groupId = "url", containerFactory = "urlListenerContainerFactory")
    void saveUrlThumbnail(Url url) {
        Map<String, String> thumbnail = new HashMap<>();
        thumbnail.put("title", url.getUrlTitle());
        thumbnail.put("url", url.getUrl());
        thumbnail.put("urlId", url.getUrlId().toString());
        try{
            Document doc = Jsoup.connect(url.getUrl().toString()).get();
            Elements elements = doc.select("meta[property^=og:]");

            for(Element el : elements) {
                switch(el.attr("property")) {
                    case "og:site_name":
                        thumbnail.put("siteName", el.attr("content"));
                        break;
                    case "og:description":
                        thumbnail.put("description", url.getUrlDescription());
                        break;
                    case "og:image":
                        thumbnail.put("image", el.attr("content"));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        url.setThumbnail(thumbnail);
        urlRepository.save(url);
    }
}
