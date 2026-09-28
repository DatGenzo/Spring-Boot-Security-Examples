package com.thanhdat.securityemail.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhdat.securityemail.entities.Role;

public interface RoleRepository
        extends JpaRepository<Role, Long> {

    Optional<Role> findByNameIgnoreCase(
            String name
    );
}
