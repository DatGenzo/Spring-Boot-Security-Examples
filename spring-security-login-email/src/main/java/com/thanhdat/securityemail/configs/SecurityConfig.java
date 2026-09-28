package com.thanhdat.securityemail.configs;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .dispatcherTypeMatchers(
                                        FORWARD,
                                        ERROR
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/login",
                                        "/css/**",
                                        "/images/**",
                                        "/favicon.ico",
                                        "/error"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/admin/**"
                                )
                                .hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                )

                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl(
                                        "/login"
                                )
                                .usernameParameter(
                                        "email"
                                )
                                .passwordParameter(
                                        "password"
                                )
                                .defaultSuccessUrl(
                                        "/",
                                        true
                                )
                                .failureUrl(
                                        "/login?error=true"
                                )
                                .permitAll()
                )

                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl(
                                        "/login?logout=true"
                                )
                                .invalidateHttpSession(
                                        true
                                )
                                .deleteCookies(
                                        "JSESSIONID"
                                )
                                .permitAll()
                );

        return http.build();
    }
}
