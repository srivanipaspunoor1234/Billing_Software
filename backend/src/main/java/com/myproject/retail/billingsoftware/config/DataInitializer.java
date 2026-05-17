package com.myproject.retail.billingsoftware.config;

import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.myproject.retail.billingsoftware.entity.UserEntity;
import com.myproject.retail.billingsoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Only creates super admin if it doesn't already exist
        if (userRepository.findByEmail("superadmin@store.com").isEmpty()) {
            UserEntity superAdmin = UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .name("Super Admin")
                .email("superadmin@store.com")
                .password(passwordEncoder.encode("SuperAdmin@123"))
                .role("ROLE_SUPER_ADMIN")
                .build();
            userRepository.save(superAdmin);
        } 
    }
}