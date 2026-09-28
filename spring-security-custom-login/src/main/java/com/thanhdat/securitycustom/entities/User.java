package com.thanhdat.securitycustom.entities;

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
            length = 100
    )
    private String username;

    @Column(
            nullable = false,
            unique = true,
            length = 200
    )
    private String email;

    @Column(
            nullable = false,
            length = 255
    )
    private String password;

    @Column(
            name = "full_name",
            nullable = false,
            columnDefinition = "nvarchar(200)"
    )
    private String fullName;

    @Column(length = 500)
    private String images;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(
            name = "role_id",
            nullable = false
    )
    private Role role;
}