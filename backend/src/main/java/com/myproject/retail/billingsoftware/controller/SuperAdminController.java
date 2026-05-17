package com.myproject.retail.billingsoftware.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.myproject.retail.billingsoftware.io.UserRequest;
import com.myproject.retail.billingsoftware.io.UserResponse;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/superadmin")
public class SuperAdminController {

    private final UserService userService;

    // Only super admin can create admins
    @PostMapping("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createAdmin(@RequestBody UserRequest request) {

    request.setRole("ROLE_ADMIN");

    UserResponse response = userService.createUser(request);

    return ApiResponse.success("Admin created successfully", response);
}

    // Only super admin can see all admins
    @GetMapping("/admins")
    public ApiResponse<List<UserResponse>> getAdmins() {

        return ApiResponse.success(
                "Admins fetched successfully",
                userService.readAdmins()
        );
    }

    // Only super admin can delete admins
   @DeleteMapping("/admins/{id}")
   public ApiResponse<String> deleteAdmin(@PathVariable String id) {

    userService.deleteUser(id);

    return ApiResponse.success("Admin deleted successfully", id);
}
   
}