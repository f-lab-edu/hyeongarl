package com.hyeongarl.repository;

import com.hyeongarl.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    void deleteExpiredTokenByUserIdAndExpiryDate(@Param("userId") Long userId, @Param("now")LocalDateTime now);

    @Query("SELECT " +
            "CASE WHEN COUNT(t) > 0 THEN true " +
            "ELSE false END " +
            "FROM Token t " +
            "WHERE t.userId= :userId " +
            "AND t.expiryDate < :now")
    boolean existsByUserIdAndExpiryDate(Long userId, LocalDateTime now);
}
