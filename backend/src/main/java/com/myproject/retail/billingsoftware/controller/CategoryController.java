package com.myproject.retail.billingsoftware.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.retail.billingsoftware.io.CategoryRequest;
import com.myproject.retail.billingsoftware.io.CategoryResponse;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // USER & ADMIN — public read
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> fetchCategories() {
        return ApiResponse.success(
                "Categories fetched successfully",
                categoryService.read()
        );
    }

    // ADMIN only — create category
    @PostMapping(value = "/admin/categories", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> addCategory(
            @Valid @RequestPart("category") CategoryRequest request,
            @RequestPart(value = "image", required = false) MultipartFile file) {

        CategoryResponse response = categoryService.add(request, file);

        return ApiResponse.success(
                "Category created successfully",
                response
        );
    }

    // ADMIN only — delete category
    @DeleteMapping("/admin/categories/{categoryId}")
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String categoryId) {
    categoryService.delete(categoryId);

    }
}