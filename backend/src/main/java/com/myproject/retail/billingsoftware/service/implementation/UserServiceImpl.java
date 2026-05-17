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

    private UserResponse convertToResponse(UserEntity user) {
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .userId(user.getUserId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .role(user.getRole())
                .build();
    }

    private UserEntity convertToEntity(UserRequest request) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .name(request.getName())
                .build();
    }

    @Override
    public String getUserRole(String email) {
        return userRepository.findByEmail(email)
                .map(UserEntity::getRole)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));    }

    @Override
    public List<UserResponse> readUsers() {
        return userRepository.findByRole("ROLE_USER")
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
        UserEntity existingUser = userRepository.findByUserId(id)
        		.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        userRepository.delete(existingUser);
    }

    @Override
    public List<UserResponse> readAdmins() {
        return userRepository.findByRole("ROLE_ADMIN")
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}