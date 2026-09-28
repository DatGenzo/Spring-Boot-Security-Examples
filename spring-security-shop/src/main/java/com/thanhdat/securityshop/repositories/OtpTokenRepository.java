package com.thanhdat.securityshop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhdat.securityshop.entities.OtpToken;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findTopByEmailIgnoreCaseAndTypeAndUsedFalseOrderByCreatedAtDesc(
            String email,
            String type
    );

    void deleteByEmailIgnoreCaseAndType(String email, String type);
}