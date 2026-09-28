package com.thanhdat.securityemail.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 120
    )
    private String email;

    @Column(
            nullable = false,
            length = 150
    )
    private String password;

    @Column(
            name = "full_name",
            nullable = false,
            length = 120,
            columnDefinition = "nvarchar(120)"
    )
    private String fullName;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt =
            LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "role_id",
            nullable = false
    )
    private Role role;
}
