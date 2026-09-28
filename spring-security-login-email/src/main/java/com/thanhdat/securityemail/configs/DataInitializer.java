package com.thanhdat.securityemail.configs;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.thanhdat.securityemail.entities.Role;
import com.thanhdat.securityemail.entities.User;
import com.thanhdat.securityemail.repositories.RoleRepository;
import com.thanhdat.securityemail.repositories.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    CommandLineRunner initializeData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,

            @Value("${app.admin.email}")
            String adminEmail,

            @Value("${app.admin.password}")
            String adminPassword
    ) {
        return arguments -> {
            Role userRole = roleRepository
                    .findByNameIgnoreCase("USER")
                    .orElseGet(() ->
                            roleRepository.save(
                                    new Role("USER")
                            )
                    );

            Role adminRole = roleRepository
                    .findByNameIgnoreCase("ADMIN")
                    .orElseGet(() ->
                            roleRepository.save(
                                    new Role("ADMIN")
                            )
                    );

            String normalizedEmail =
                    adminEmail
                            .trim()
                            .toLowerCase(Locale.ROOT);

            if (!userRepository
                    .existsByEmailIgnoreCase(
                            normalizedEmail
                    )) {

                User admin = new User();

                admin.setEmail(normalizedEmail);
                admin.setFullName(
                        "Đỗ Thành Đạt"
                );

                admin.setPassword(
                        passwordEncoder.encode(
                                adminPassword
                        )
                );

                admin.setRole(adminRole);
                admin.setEnabled(true);

                userRepository.save(admin);
            }

            // Giữ biến userRole được tạo rõ ràng
            // để database có đủ hai Role.
            userRole.getName();
        };
    }
}
