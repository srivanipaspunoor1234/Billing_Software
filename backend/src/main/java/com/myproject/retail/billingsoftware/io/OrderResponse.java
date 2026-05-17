package com.myproject.retail.billingsoftware.io;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

private String orderId;
private String customerName;
private String phoneNumber;
private List<OrderResponse.OrderItemResponse> items;
private Double subtotal;
private Double tax;
private Double grandTotal;
private PaymentMethod  paymentMethod;
private LocalDateTime createdAt;
private PaymentDetails paymentDetails;
private boolean success; 

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public static class OrderItemResponse {

	private String itemId;
    private String name;
    private Double price;
    private Integer quantity;


}
}
