package com.hyeongarl.config;

import com.hyeongarl.error.UserUnInvalidException;
import com.hyeongarl.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ApiFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    // HTTP 요청에서 토큰을 추출하여 유효성을 검사하고 인증되지 않은 경우 예외를 발생
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("token");

        if(token == null) {
            throw new UserUnInvalidException();
        }

        log.info("인증된 사용자");
        filterChain.doFilter(request, response);
    }
}
