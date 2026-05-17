package com.myproject.retail.billingsoftware.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.myproject.retail.billingsoftware.io.OrderRequest;
import com.myproject.retail.billingsoftware.io.OrderResponse;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {

    OrderResponse response = orderService.createOrder(request);

    return ApiResponse.success("Order created successfully", response);
}

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<String> deleteOrder(@PathVariable String orderId) {

        orderService.deleteOrder(orderId);

        return ApiResponse.success("Order deleted successfully", orderId);
    }

    @GetMapping("/latest")
    public ApiResponse<List<OrderResponse>> getLatestOrders() {

        return ApiResponse.success(
                "Latest orders fetched",
                orderService.getLatestOrders()
        );
    }
    
    @PutMapping("/{orderId}/failed")
    public ApiResponse<String> markOrderFailed(@PathVariable String orderId) {

        orderService.markOrderFailed(orderId);

        return ApiResponse.success("Order marked as failed", orderId);
    }
    
    @GetMapping
    public ApiResponse<Page<OrderResponse>> getOrdersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success("Orders fetched", orderService.getOrdersPaginated(page, size));
    }
    
}