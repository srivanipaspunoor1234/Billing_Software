package com.myproject.retail.billingsoftware.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.retail.billingsoftware.response.ApiResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/test")
    public ApiResponse<String> testAdmin() {
	    return ApiResponse.success("Admin access successful", "OK");
    }
}