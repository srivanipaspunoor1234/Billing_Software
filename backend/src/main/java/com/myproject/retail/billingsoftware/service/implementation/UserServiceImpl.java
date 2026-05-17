package com.myproject.retail.billingsoftware.service.implementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myproject.retail.billingsoftware.entity.UserEntity;
import com.myproject.retail.billingsoftware.io.UserRequest;
import com.myproject.retail.billingsoftware.io.UserResponse;
import com.myproject.retail.billingsoftware.repository.UserRepository;
import com.myproject.retail.billingsoftware.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {

        UserEntity newUser = convertToEntity(request);
        newUser = userRepository.save(newUser);

        return convertToResponse(newUser);
    }

    private UserResponse convertToResponse(UserEntity newUser) {
        return UserResponse.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .userId(newUser.getUserId())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .role(newUser.getRole())
                .build();
    }

    private UserEntity convertToEntity(UserRequest request) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())   // better than email as ID
                .email(request.getEmail())              // ✅ FIXED
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole().toUpperCase())
                .name(request.getName())
                .build();
    }

    @Override
    public String getUserRole(String email) {
        return userRepository.findByEmail(email)
                .map(UserEntity::getRole)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserResponse> readUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
    	UserEntity existingUser = userRepository.findByUserId(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(existingUser);
    }

    @Override
    public List<UserResponse> readAdmins() {

        return userRepository.findAll()
                .stream()
                .filter(user -> "ADMIN".equalsIgnoreCase(user.getRole()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}