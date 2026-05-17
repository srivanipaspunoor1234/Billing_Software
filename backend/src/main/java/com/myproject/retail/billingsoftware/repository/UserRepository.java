package com.myproject.retail.billingsoftware.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.retail.billingsoftware.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserId(String userId);
    List<UserEntity> findByRole(String role);   
}