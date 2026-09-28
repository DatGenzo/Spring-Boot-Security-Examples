package com.thanhdat.securitycustom.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thanhdat.securitycustom.entities.Role;
import com.thanhdat.securitycustom.entities.User;
import com.thanhdat.securitycustom.repositories.RoleRepository;
import com.thanhdat.securitycustom.repositories.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.user-password}")
    private String userPassword;

    @Value("${app.seed.admin-password}")
    private String adminPassword;

    public DataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        validatePassword(
                userPassword,
                "USER_INITIAL_PASSWORD"
        );

        validatePassword(
                adminPassword,
                "ADMIN_INITIAL_PASSWORD"
        );

        Role userRole = findOrCreateRole(
                "ROLE_USER"
        );

        Role adminRole = findOrCreateRole(
                "ROLE_ADMIN"
        );

        createUserIfMissing(
                "user01",
                "user01@gmail.com",
                "Đỗ Thành Đạt",
                "/images/avatar-user.svg",
                userPassword,
                userRole
        );

        createUserIfMissing(
                "admin01",
                "admin01@gmail.com",
                "System Administrator",
                "/images/avatar-admin.svg",
                adminPassword,
                adminRole
        );
    }

    private Role findOrCreateRole(String name) {
        return roleRepository
                .findByName(name)
                .orElseGet(
                        () -> {
                            Role role = new Role();
                            role.setName(name);

                            return roleRepository.save(role);
                        }
                );
    }

    private void createUserIfMissing(
            String username,
            String email,
            String fullName,
            String image,
            String rawPassword,
            Role role
    ) {
        if (
                userRepository.existsByUsernameIgnoreCase(
                        username
                )
                || userRepository.existsByEmailIgnoreCase(
                        email
                )
        ) {
            return;
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setImages(image);
        user.setEnabled(true);
        user.setRole(role);

        user.setPassword(
                passwordEncoder.encode(rawPassword)
        );

        userRepository.save(user);
    }

    private void validatePassword(
            String password,
            String variableName
    ) {
        if (
                password == null
                || password.isBlank()
        ) {
            throw new IllegalStateException(
                    "Missing environment variable: "
                            + variableName
            );
        }
    }
}