package com.hyeongarl.repository;

import com.hyeongarl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);

    @Query("SELECT CASE " +
            "WHEN COUNT(u) > 0 THEN true " +
            "ELSE false END FROM User u " +
            "WHERE u.userEmail = :userEmail")
    boolean existsByUserEmail(String userEmail);
}
