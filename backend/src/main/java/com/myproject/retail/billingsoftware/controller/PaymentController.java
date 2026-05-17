package com.myproject.retail.billingsoftware.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.myproject.retail.billingsoftware.io.OrderResponse;
import com.myproject.retail.billingsoftware.io.PaymentRequest;
import com.myproject.retail.billingsoftware.io.PaymentVerificationRequest;
import com.myproject.retail.billingsoftware.io.RazorpayOrderResponse;
import com.myproject.retail.billingsoftware.response.ApiResponse;
import com.myproject.retail.billingsoftware.service.OrderService;
import com.myproject.retail.billingsoftware.service.RazorpayService;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;
    private final OrderService orderService;

   @PostMapping("/create-order")
   @ResponseStatus(HttpStatus.CREATED)
   public ApiResponse<RazorpayOrderResponse> createRazorpayOrder(
        @RequestBody PaymentRequest request) throws RazorpayException {

    RazorpayOrderResponse response =
            razorpayService.createOrder(request.getAmount(), request.getCurrency());

    return ApiResponse.success("Razorpay order created", response);
}
    
   @PostMapping("/verify")
   public ApiResponse<OrderResponse> verifyPayment(
           @RequestBody PaymentVerificationRequest request) {

       OrderResponse response = orderService.verifyPayment(request);

       return ApiResponse.success("Payment verified successfully", response);
   }
}