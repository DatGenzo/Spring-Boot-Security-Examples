package com.thanhdat.securityemail.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thanhdat.securityemail.entities.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    boolean existsByEmailIgnoreCase(
            String email
    );

    Optional<User> findByEmailIgnoreCase(
            String email
    );

    @Query("""
            SELECT user
            FROM User user
            JOIN FETCH user.role
            WHERE LOWER(user.email) =
                  LOWER(:email)
            """)
    Optional<User> findByEmailWithRole(
            @Param("email") String email
    );
}
