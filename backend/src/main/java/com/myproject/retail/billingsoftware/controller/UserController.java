package com.myproject.retail.billingsoftware.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.myproject.retail.billingsoftware.io.UserRequest;
import com.myproject.retail.billingsoftware.io.UserResponse;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

   @PostMapping("/register")
   @ResponseStatus(HttpStatus.CREATED)
   public ApiResponse<UserResponse> registerUser(@Valid @RequestBody UserRequest request) {

    UserResponse response = userService.createUser(request);

    return ApiResponse.success("User registered successfully", response);
}

   @GetMapping("/users")
   public ApiResponse<List<UserResponse>> readUsers() {

       return ApiResponse.success(
               "Users fetched successfully",
               userService.readUsers()
       );
   }

   @DeleteMapping("/users/{id}")
   public ApiResponse<String> deleteUser(@PathVariable String id) {

       userService.deleteUser(id);

       return ApiResponse.success("User deleted successfully", id);
   }
}