package com.thanhdat.securitycustom.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhdat.securitycustom.entities.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(
            String username
    );

    Optional<User> findByEmailIgnoreCase(
            String email
    );

    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(
            String username,
            String email
    );

    boolean existsByUsernameIgnoreCase(
            String username
    );

    boolean existsByEmailIgnoreCase(
            String email
    );
}