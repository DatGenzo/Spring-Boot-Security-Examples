package com.thanhdat.securityshop.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thanhdat.securityshop.entities.Role;
import com.thanhdat.securityshop.entities.User;
import com.thanhdat.securityshop.repositories.RoleRepository;
import com.thanhdat.securityshop.repositories.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminEmail;
    private final String adminPassword;
    private final String adminFullName;

    public DataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.username}") String adminUsername,
            @Value("${app.admin.email}") String adminEmail,
            @Value("${app.admin.password}") String adminPassword,
            @Value("${app.admin.full-name}") String adminFullName
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminFullName = adminFullName;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role userRole = findOrCreateRole("ROLE_USER");
        Role adminRole = findOrCreateRole("ROLE_ADMIN");

        if (!userRepository.existsByUsernameIgnoreCase(adminUsername)
                && !userRepository.existsByEmailIgnoreCase(adminEmail)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail.toLowerCase());
            admin.setFullName(adminFullName);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEnabled(true);
            admin.setRole(adminRole);
            userRepository.save(admin);
        }

        if (userRole.getId() == null) {
            throw new IllegalStateException("Không thể khởi tạo ROLE_USER");
        }
    }

    private Role findOrCreateRole(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    return roleRepository.save(role);
                });
    }
}