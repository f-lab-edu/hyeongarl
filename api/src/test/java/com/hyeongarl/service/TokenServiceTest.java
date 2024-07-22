package com.hyeongarl.service;

import com.hyeongarl.entity.Token;
import com.hyeongarl.entity.User;
import com.hyeongarl.repository.TokenRepository;
import com.hyeongarl.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private KafkaTemplate<String, Long> kafkaTemplate;

    @Test
    public void testLogin_Kafka() {
        User user = new User();
        user.setUserId(1L);
        user.setUserEmail("testKafka@email.com");
        user.setPassword("testKafka");

        Token tokenEntity = Token.builder()
                .token("fixedToken")
                .userId(1L)
                .tokenId(1L)
                .expiryDate(LocalDateTime.now().plusHours(3))
                .build();

        when(userRepository.findByUserEmail(user.getUserEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenRepository.existsByUserIdAndExpiryDate(anyLong(), any(LocalDateTime.class))).thenReturn(false);
        when(tokenRepository.save(any(Token.class))).thenReturn(tokenEntity);

        String resultToken = tokenService.login(user);

        verify(kafkaTemplate, times(1)).send("login-topic", resultToken,1L);
    }
}
