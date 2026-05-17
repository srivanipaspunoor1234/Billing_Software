package com.myproject.retail.billingsoftware.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

import com.myproject.retail.billingsoftware.io.DashboardResponse;
import com.myproject.retail.billingsoftware.io.OrderResponse;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final OrderService orderService;

    // ADMIN + SUPER ADMIN dashboard
   @GetMapping
public ApiResponse<DashboardResponse> getDashboardData() {

    LocalDate today = LocalDate.now();

    Double todaySales = orderService.sumSalesByDate(today);
    Long todayOrderCount = orderService.countByOrderDate(today);
    List<OrderResponse> recentOrders = orderService.findRecentOrders();

    DashboardResponse response = new DashboardResponse(
            todaySales != null ? todaySales : 0.0,
            todayOrderCount != null ? todayOrderCount : 0L,
            0L,
            0.0,
            recentOrders
    );

    return ApiResponse.success("Dashboard loaded", response);
}

    // USER dashboard
    @GetMapping("/user")
public ApiResponse<DashboardResponse> getUserDashboardData() {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    LocalDate today = LocalDate.now();

    Double todaySales = orderService.sumSalesByDateAndCreatedBy(today, username);
    Long todayOrderCount = orderService.countByOrderDateAndCreatedBy(today, username);
    Long todayItemsSold = orderService.sumItemsSoldByDateAndCreatedBy(today, username);

    List<OrderResponse> recentOrders = orderService.findRecentOrdersByUser(username);

    double avgBillValue = todayOrderCount > 0 ? todaySales / todayOrderCount : 0.0;

    DashboardResponse response = new DashboardResponse(
            todaySales,
            todayOrderCount,
            todayItemsSold,
            avgBillValue,
            recentOrders
    );

    return ApiResponse.success("User dashboard loaded", response);
}
    
}