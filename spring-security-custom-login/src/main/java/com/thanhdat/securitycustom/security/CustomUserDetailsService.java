package com.thanhdat.securitycustom.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thanhdat.securitycustom.entities.User;
import com.thanhdat.securitycustom.repositories.UserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String usernameOrEmail
    ) throws UsernameNotFoundException {

        String login = usernameOrEmail.trim();

        User user = userRepository
                .findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        login,
                        login
                )
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "Không tìm thấy tài khoản: "
                                        + login
                        )
                );

        return new CustomUserDetails(user);
    }
}