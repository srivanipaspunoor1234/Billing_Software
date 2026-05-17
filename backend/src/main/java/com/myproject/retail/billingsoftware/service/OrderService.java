package com.myproject.retail.billingsoftware.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.myproject.retail.billingsoftware.io.OrderRequest;
import com.myproject.retail.billingsoftware.io.OrderResponse;
import com.myproject.retail.billingsoftware.io.PaymentVerificationRequest;

public interface OrderService {

	
	void deleteOrder(String orderId);
	
	List<OrderResponse> getLatestOrders();

	OrderResponse createOrder(OrderRequest request);

	OrderResponse verifyPayment(PaymentVerificationRequest request);
	
	List<OrderResponse> findRecentOrders();

	void markOrderFailed(String orderId);
	
	Double sumSalesByDate(LocalDate date);
	
	Long countByOrderDate(LocalDate date);
	
	Double sumSalesByDateAndCreatedBy(LocalDate date, String createdBy);
	
	Long countByOrderDateAndCreatedBy(LocalDate date, String createdBy);
	
	Long sumItemsSoldByDateAndCreatedBy(LocalDate date, String createdBy);
	
	List<OrderResponse> findRecentOrdersByUser(String createdBy);

	Page<OrderResponse> getOrdersPaginated(int page, int size);
	
}
