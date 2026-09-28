package com.thanhdat.securityshop.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thanhdat.securityshop.entities.User;
import com.thanhdat.securityshop.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        String login = usernameOrEmail.trim();

        User user = userRepository
                .findByUsernameIgnoreCaseOrEmailIgnoreCase(login, login)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        return new CustomUserDetails(user);
    }
}