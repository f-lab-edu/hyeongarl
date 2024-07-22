package com.hyeongarl.service;

import com.hyeongarl.entity.Token;
import com.hyeongarl.entity.User;
import com.hyeongarl.error.UserNotFoundException;
import com.hyeongarl.repository.TokenRepository;
import com.hyeongarl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final KafkaTemplate<String, Long> kafkaTemplate;

    public String login(User userRequest) {
        LocalDateTime now = LocalDateTime.now();

        // 사용자 정보 확인
        User user = userRepository.findByUserEmail(userRequest.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        // 비밀번호 불일치
        if(!bCryptPasswordEncoder.matches(userRequest.getPassword(),(user.getPassword()))) {
            throw new UserNotFoundException();
        }

        // 만료된 토큰 있는 경우 삭제
        if(tokenRepository.existsByUserIdAndExpiryDate(user.getUserId(), now)) {
            tokenRepository.deleteExpiredTokenByUserIdAndExpiryDate(user.getUserId(), now);
        }

        Token validateToken = tokenRepository.findByUserIdAndExpiryDate(user.getUserId(), now);

        String token;
        // 인증 토큰 없는 경우
        if(validateToken == null) {
            token = UUID.randomUUID() + "-" + System.currentTimeMillis();

            Token saveToken = Token.builder()
                    .token(token)
                    .userId(user.getUserId())
                    .build();

            tokenRepository.save(saveToken);
        } else {
            token = validateToken.getToken();
        }
        sendMessage("login-topic", token, user.getUserId());
        return token;
    }

    public Long getUserId() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getPrincipal().toString();

        return tokenRepository.findUserIDByToken(token);
    }

    public void sendMessage(String topic, String token, Long userId) {
        kafkaTemplate.send(topic, token, userId);
    }
}
