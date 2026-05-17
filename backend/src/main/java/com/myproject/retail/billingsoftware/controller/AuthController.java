package com.myproject.retail.billingsoftware.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.myproject.retail.billingsoftware.io.AuthRequest;
import com.myproject.retail.billingsoftware.io.AuthResponse;
import com.myproject.retail.billingsoftware.io.UserRequest;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.UserService;
import com.myproject.retail.billingsoftware.service.implementation.AppUserDetailsService;
import com.myproject.retail.billingsoftware.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

   @PostMapping("/login")
public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
    } catch (DisabledException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Account is disabled. Contact support."));

    } catch (BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid email or password"));
    }

    final UserDetails userDetails =
            appUserDetailsService.loadUserByUsername(request.getEmail());

    final String jwtToken = jwtUtil.generateToken(userDetails);
    final String role = userService.getUserRole(request.getEmail());

    AuthResponse response = new AuthResponse(request.getEmail(), jwtToken, role);

    return ResponseEntity.ok(
            ApiResponse.success("Login successful", response)
    );
}
    
   @PostMapping("/register")
public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRequest request) {

    request.setRole("ROLE_USER");

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                    "User registered successfully",
                    userService.createUser(request)
            ));
}

}