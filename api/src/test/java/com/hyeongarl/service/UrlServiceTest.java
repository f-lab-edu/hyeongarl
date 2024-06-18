package com.hyeongarl.service;

import com.hyeongarl.dto.UrlRequestDto;
import com.hyeongarl.entity.Url;
import com.hyeongarl.error.UrlAlreadyExistException;
import com.hyeongarl.error.UrlInvalidException;
import com.hyeongarl.error.UrlNotFoundException;
import com.hyeongarl.repository.UrlRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
    @ExtendWith(MockitoExtension.class)
        JUnit5의 확장 모델을 활용하여 Mockito를 사용할 수 있다.
        MockExtension : Mockito에 대한 설정을 초기화 및 @Mockito 처리
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {
    /*
        @Mock
            Mockito를 사용해 가짜 객체를 생성
     */
    @Mock
    private UrlRepository urlRepository;

    /*
        @InjectMocks
            실제 객체로 만들어 필요한 의존성 객체를 주입
     */
    @InjectMocks
    private UrlService urlService;

    @Test
    @DisplayName("get_success")
    void getUrl_success() {
        Url url = new Url(1L, "http://getUrl.com",
                "getUrl Title", "getUrl Description",
                1L, 1L, null, null);

        when(urlRepository.findById(url.getUrlId())).thenReturn(Optional.of(url));

        Url result = urlService.getUrl(1L);

        assertNotNull(result);
        assertEquals(url.getUrl(), result.getUrl());
        verify(urlRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("get_fail_notFound")
    void getUrl_fail_notFound() {
        when(urlRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlService.getUrl(any(Long.class)));

        verify(urlRepository, times(1)).findById(any(Long.class));
    }

    @Nested
    @DisplayName("Url 등록")
    class saveUrlTests {
        @Test
        @DisplayName("save_success")
        void saveUrl_success() {
            UrlRequestDto urlRequest = new UrlRequestDto(
                    "https://www.naver.com",
                    "saveUrlTitle",
                    "saveUrl_Description",
                    1L);

            when(urlRepository.existsByUrl(urlRequest.getUrl(), 1L)).thenReturn(false);
            when(urlRepository.save(any(Url.class))).thenReturn(urlRequest.toEntity());

            Url url = urlService.save(urlRequest.toEntity());
            assertNotNull(url);
            assertEquals(urlRequest.getUrl(), url.getUrl());
            assertEquals(urlRequest.getUrlTitle(), url.getUrlTitle());
            assertEquals(urlRequest.getUrlDescription(), url.getUrlDescription());

            verify(urlRepository, times(1)).save(any(Url.class));
        }

        @Test
        @DisplayName("save_fail_invalid")
        void saveUrl_fail_invalid() {
            UrlRequestDto urlRequest = new UrlRequestDto(
                    null,
                    "saveUrlTitle",
                    "saveUrlDescription",
                    1L);

            assertThrows(UrlInvalidException.class, () -> urlService.save(urlRequest.toEntity()));

            verify(urlRepository, never()).save(any(Url.class));
        }

        @Test
        @DisplayName("save_fail_alreadyExist")
        void saveUrl_fail_alreadyExist() {
            UrlRequestDto urlRequest = new UrlRequestDto(
                    "https://www.naver.com",
                    "saveUrlTitle",
                    "saveUrl_Description",
                    1L);

            when(urlRepository.existsByUrl(urlRequest.getUrl(), 1L)).thenReturn(true);

            assertThrows(UrlAlreadyExistException.class, () -> urlService.save(urlRequest.toEntity()));

            verify(urlRepository, never()).save(any(Url.class));
        }
    }

    @Nested
    @DisplayName("Url 수정")
    class updateUrlTests {
        @Test
        @DisplayName("update_success")
        void updateUrl_success() {
            UrlRequestDto urlRequest = new UrlRequestDto(
                    "http://update.com",
                    "update Title",
                    "update Description",
                    1L);
            Url existingUrl = new Url(
                    1L, "http://exist.com",
                    "exist Title", "exist Description",
                    1L, 1L, null, null);
            Url updateUrl = new Url(
                    1L, "http://updatecom",
                    "update Title", "update Description",
                    1L, 1L, null, null);

            when(urlRepository.findById(1L)).thenReturn(Optional.of(existingUrl));
            when(urlRepository.save(any(Url.class))).thenReturn(updateUrl);

            Url result = urlService.updateUrl(1L, urlRequest);

            assertNotNull(result);
            assertEquals(updateUrl.getUrl(), result.getUrl());
            verify(urlRepository, times(1)).save(any(Url.class));
        }

        @Test
        @DisplayName("update_fail_notFound")
        void updateUrl_fail_notFound() {
            UrlRequestDto urlRequest = new UrlRequestDto(
                    "http://update.com",
                    "update Title",
                    "update Description",
                    1L);

            when(urlRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(UrlNotFoundException.class, () -> urlService.updateUrl(1L, urlRequest));

            verify(urlRepository, never()).save(any(Url.class));
        }
    }

    @Nested
    @DisplayName("Url 삭제")
    class deleteUrlTests {
        @Test
        @DisplayName("delete_success")
        void deleteUrl_success() {
            doNothing().when(urlRepository).deleteById(1L);
            urlService.deleteUrl(1L);
            verify(urlRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("delete_fail_errorDb")
        void deleteUrl_fail_databaseError() {
            doThrow(new RuntimeException("삭제중 오류가 발생했습니다.")).when(urlRepository).deleteById(1L);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> urlService.deleteUrl(1L));

            assertEquals("삭제중 오류가 발생했습니다.", exception.getMessage());
            verify(urlRepository, times(1)).deleteById(1L);
        }
    }
}
