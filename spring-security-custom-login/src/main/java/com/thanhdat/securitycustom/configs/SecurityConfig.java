package com.thanhdat.securitycustom.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.thanhdat.securitycustom.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider provider
    ) throws Exception {

        http
                .authenticationProvider(provider)

                .authorizeHttpRequests(
                        authorize -> authorize

                                .requestMatchers(
                                        "/login",
                                        "/access-denied",
                                        "/css/**",
                                        "/images/**",
                                        "/error"
                                )
                                .permitAll()

                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                )

                .formLogin(
                        form -> form

                                .loginPage("/login")

                                .loginProcessingUrl("/login")

                                .usernameParameter("username")

                                .passwordParameter("password")

                                .defaultSuccessUrl(
                                        "/",
                                        true
                                )

                                .failureUrl(
                                        "/login?error=true"
                                )

                                .permitAll()
                )

                .logout(
                        logout -> logout

                                .logoutUrl("/logout")

                                .logoutSuccessUrl(
                                        "/login?logout=true"
                                )

                                .invalidateHttpSession(true)

                                .deleteCookies("JSESSIONID")

                                .permitAll()
                )

                .exceptionHandling(
                        exception -> exception

                                .accessDeniedPage(
                                        "/access-denied"
                                )
                );

        return http.build();
    }
}