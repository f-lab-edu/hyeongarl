package com.hyeongarl.config;

import com.hyeongarl.entity.Token;
import com.hyeongarl.error.UserUnInvalidException;
import com.hyeongarl.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;

    // HTTP 요청에서 토큰을 추출하여 유효성을 검사하고 인증되지 않은 경우 예외를 발생
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if(requestURI.startsWith("/login") || requestURI.startsWith("/user")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 확인
        String token = request.getHeader("token");
        if(token == null) {
            throw new UserUnInvalidException();
        }

        Token validToken = tokenRepository.findByTokenAndExpiryDate(token, LocalDateTime.now())
                .orElseThrow(UserUnInvalidException::new);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                validToken.getToken(), null, new ArrayList<>()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
