package com.hyeongarl.service;

import com.hyeongarl.entity.User;
import com.hyeongarl.error.UserAlreadyExistException;
import com.hyeongarl.error.UserNotFoundException;
import com.hyeongarl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User save(User user) {
        // 이미 존재하는 이메일인 경우
        if(userRepository.existsByUserEmail(user.getUserEmail())) {
            throw new UserAlreadyExistException();
        }

        return userRepository.save(User.builder()
                .userEmail(user.getUserEmail())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build());
    }

    // userId로 유저 검색
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    // userEmail로 유저 검색
    public User findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
