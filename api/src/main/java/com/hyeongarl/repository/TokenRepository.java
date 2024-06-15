package com.hyeongarl.repository;

import com.hyeongarl.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t " +
            "FROM Token t " +
            "WHERE t.userId= :userId " +
            "AND t.expiryDate > :now")
    Token findByUserIdAndExpiryDate(Long userId, LocalDateTime now);

    @Modifying
    @Transactional
    @Query("DELETE " +
            "FROM Token " +
            "WHERE userId= :userId " +
            "AND expiryDate < :now")
    void deleteExpiredTokenByUserIdAndExpiryDate(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    // 만료된 토큰 확인
    @Query("SELECT " +
            "CASE WHEN COUNT(t) > 0 THEN true " +
            "ELSE false END " +
            "FROM Token t " +
            "WHERE t.userId= :userId " +
            "AND t.expiryDate < :now")
    boolean existsByUserIdAndExpiryDate(Long userId, LocalDateTime now);

    // 유효한 토큰 확인
    @Query("SELECT t " +
            "FROM Token t " +
            "WHERE t.token = :token " +
            "AND t.expiryDate > :now")
    Optional<Token> findByTokenAndExpiryDate(String token, LocalDateTime now);

    @Query("SELECT t.userId " +
            "FROM Token t " +
            "WHERE t.token = :token")
    Long findUserIDByToken(String token);
}
