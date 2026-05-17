package com.myproject.retail.billingsoftware.service.implementation;

import java.util.Collections;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myproject.retail.billingsoftware.entity.UserEntity;
import com.myproject.retail.billingsoftware.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Email not found for the email: " + email
                        )
                );

        return new User(
                existingUser.getEmail(),
                existingUser.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority(existingUser.getRole())
                )
        );
    }
}