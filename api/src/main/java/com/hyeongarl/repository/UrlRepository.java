package com.hyeongarl.repository;

import com.hyeongarl.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Page<Url> findAllByUserId(Pageable pageable, Long userId);

    @Query("SELECT CASE " +
            "WHEN COUNT(u) > 0 THEN true " +
            "ELSE false END FROM Url u  " +
            "WHERE u.url = :url " +
            "AND u.userId = :userId")
    boolean existsByUrl(String url, Long userId);

    Optional<Url> findByUrlIdAndUserId(Long urlId, Long userId);
}
