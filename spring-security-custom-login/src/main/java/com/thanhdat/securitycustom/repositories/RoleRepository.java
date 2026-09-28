package com.thanhdat.securitycustom.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhdat.securitycustom.entities.Role;

public interface RoleRepository
        extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}