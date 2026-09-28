package com.thanhdat.securityshop.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhdat.securityshop.dtos.UserDTO;
import com.thanhdat.securityshop.entities.Role;
import com.thanhdat.securityshop.entities.User;
import com.thanhdat.securityshop.mappers.UserMapper;
import com.thanhdat.securityshop.repositories.ProductRepository;
import com.thanhdat.securityshop.repositories.RoleRepository;
import com.thanhdat.securityshop.repositories.UserRepository;
import com.thanhdat.securityshop.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ProductRepository productRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            @Value("${app.user.default-password:123456}") String defaultPassword
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "id")
        );

        return userRepository.search(normalize(keyword), pageable)
                .map(this::toDtoWithProductCount);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return toDtoWithProductCount(findEntity(id));
    }

    @Override
    @Transactional
    public UserDTO create(UserDTO dto) {
        validateUnique(dto, null);

        User user = userMapper.toEntity(dto);
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setFullName(dto.getFullName().trim());
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setRole(findRole(dto.getRoleName()));

        return toDtoWithProductCount(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User user = findEntity(id);
        validateUnique(dto, id);

        userMapper.updateEntity(dto, user);
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setFullName(dto.getFullName().trim());
        user.setRole(findRole(dto.getRoleName()));

        return toDtoWithProductCount(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = findEntity(id);
        long productCount = productRepository.countByUserId(id);

        if (productCount > 0) {
            throw new IllegalArgumentException(
                    "Không thể xóa User đang có " + productCount + " Product"
            );
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    private User findEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));
    }

    private Role findRole(String roleName) {
        String effectiveRole = roleName == null || roleName.isBlank()
                ? "ROLE_USER"
                : roleName;

        return roleRepository.findByName(effectiveRole)
                .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại"));
    }

    private void validateUnique(UserDTO dto, Long currentId) {
        boolean usernameExists = currentId == null
                ? userRepository.existsByUsernameIgnoreCase(dto.getUsername())
                : userRepository.existsByUsernameIgnoreCaseAndIdNot(
                        dto.getUsername(), currentId
                );

        if (usernameExists) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        boolean emailExists = currentId == null
                ? userRepository.existsByEmailIgnoreCase(dto.getEmail())
                : userRepository.existsByEmailIgnoreCaseAndIdNot(
                        dto.getEmail(), currentId
                );

        if (emailExists) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
    }

    private UserDTO toDtoWithProductCount(User user) {
        UserDTO dto = userMapper.toDto(user);
        dto.setProductCount(productRepository.countByUserId(user.getId()));
        return dto;
    }

    private String normalize(String keyword) {
        return keyword == null ? "" : keyword.trim();
    }
}