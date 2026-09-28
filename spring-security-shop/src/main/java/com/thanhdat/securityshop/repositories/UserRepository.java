package com.thanhdat.securityshop.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thanhdat.securityshop.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(
            String username,
            String email
    );

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    @Query("""
            select u
            from User u
            where lower(u.username) like lower(concat('%', :keyword, '%'))
               or lower(u.email) like lower(concat('%', :keyword, '%'))
               or lower(u.fullName) like lower(concat('%', :keyword, '%'))
            """)
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);
}