package com.myproject.retail.billingsoftware.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.retail.billingsoftware.entity.OrderEntity;
import com.myproject.retail.billingsoftware.entity.OrderItemEntity;
import com.myproject.retail.billingsoftware.io.OrderRequest;
import com.myproject.retail.billingsoftware.io.OrderResponse;
import com.myproject.retail.billingsoftware.io.PaymentDetails;
import com.myproject.retail.billingsoftware.io.PaymentMethod;
import com.myproject.retail.billingsoftware.io.PaymentVerificationRequest;
import com.myproject.retail.billingsoftware.repository.OrderEntityRepository;
import com.myproject.retail.billingsoftware.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import com.myproject.retail.billingsoftware.io.PaymentDetails.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderEntityRepository orderEntityRepository;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    private static final int DUPLICATE_WINDOW_SECONDS = 10;

    // CREATE ORDER
    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        if (request.getPhoneNumber() != null && request.getGrandTotal() != null) {

            LocalDateTime windowStart = LocalDateTime.now()
                    .minusSeconds(DUPLICATE_WINDOW_SECONDS);

            List<OrderEntity> recentOrders = orderEntityRepository
                    .findTop5ByOrderByCreatedAtDesc();

            for (OrderEntity order : recentOrders) {

                boolean samePhone = order.getPhoneNumber() != null
                        && order.getPhoneNumber().equals(request.getPhoneNumber());

                boolean sameAmount = order.getGrandTotal() != null
                        && order.getGrandTotal().equals(request.getGrandTotal());

                boolean isPending = order.getPaymentDetails() != null
                        && order.getPaymentDetails().getStatus()
                                == PaymentDetails.PaymentStatus.PENDING;

                boolean isWithinWindow = order.getCreatedAt() != null
                        && order.getCreatedAt().isAfter(windowStart);

                if (samePhone && sameAmount && isPending && isWithinWindow) {
                    return convertToResponse(order);
                }
            }
        }

        OrderEntity newOrder = convertToOrderEntity(request);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStatus(
                newOrder.getPaymentMethod() == PaymentMethod.CASH
                        ? PaymentDetails.PaymentStatus.COMPLETED
                        : PaymentDetails.PaymentStatus.PENDING
        );

        newOrder.setPaymentDetails(paymentDetails);

        List<OrderItemEntity> orderItems = request.getCartItems()
                .stream()
                .map(item -> {
                    OrderItemEntity entity = convertToOrderItemEntity(item);
                    entity.setOrder(newOrder);
                    return entity;
                })
                .collect(Collectors.toList());

        newOrder.setItems(orderItems);

        OrderEntity savedOrder = orderEntityRepository.save(newOrder);

        return convertToResponse(savedOrder);
    }

    // DELETE ORDER
    @Override
    public void deleteOrder(String orderId) {

        OrderEntity existingOrder = orderEntityRepository
                .findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderEntityRepository.delete(existingOrder);
    }

    // GET ALL ORDERS
    @Override
    @Transactional
    public List<OrderResponse> getLatestOrders() {

        return orderEntityRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // VERIFY PAYMENT
  @Override
@Transactional
public OrderResponse verifyPayment(PaymentVerificationRequest request) {
	  log.info("VERIFY PAYMENT REQUEST: {}", request);
    if (request.getOrderId() == null || request.getOrderId().isBlank()) {
        log.error("Payment verification failed: orderId is null or empty");
        throw new RuntimeException("OrderId is required for payment verification");
    }

    OrderEntity existingOrder = orderEntityRepository
            .findByOrderId(request.getOrderId())
            .orElseThrow(() -> {
                log.error("Order not found for verification. orderId={}", request.getOrderId());
                return new RuntimeException("Order not found: " + request.getOrderId());
            });

    PaymentDetails paymentDetails = existingOrder.getPaymentDetails();

    if (paymentDetails == null) {
        paymentDetails = new PaymentDetails();
        paymentDetails.setStatus(PaymentDetails.PaymentStatus.PENDING);
        existingOrder.setPaymentDetails(paymentDetails);
    }

    if (paymentDetails.getStatus() == PaymentDetails.PaymentStatus.COMPLETED) {
        return convertToResponse(existingOrder);
    }

    boolean isValid = verifyRazorpaySignature(
            request.getRazorpayOrderId(),
            request.getRazorpayPaymentId(),
            request.getRazorpaySignature()
    );

    if (isValid) {
        paymentDetails.setRazorpayOrderId(request.getRazorpayOrderId());
        paymentDetails.setRazorpayPaymentId(request.getRazorpayPaymentId());
        paymentDetails.setRazorpaySignature(request.getRazorpaySignature());
        paymentDetails.setStatus(PaymentDetails.PaymentStatus.COMPLETED);
    } else {
        paymentDetails.setStatus(PaymentDetails.PaymentStatus.FAILED);
    }

    existingOrder.setPaymentDetails(paymentDetails);

    OrderEntity saved = orderEntityRepository.save(existingOrder);
    return convertToResponse(saved);
}

    // ENTITY CONVERSIONS

    private OrderEntity convertToOrderEntity(OrderRequest request) {

    	if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
    	    throw new RuntimeException("Customer name is required");
    	}

    	String customerName = request.getCustomerName().trim();
        return OrderEntity.builder()
                .customerName(customerName)
                .phoneNumber(request.getPhoneNumber())
                .subtotal(request.getSubtotal())
                .tax(request.getTax())
                .grandTotal(request.getGrandTotal())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .orderId(UUID.randomUUID().toString())
                .build();
    }

    private OrderItemEntity convertToOrderItemEntity(
            OrderRequest.OrderItemRequest orderItemRequest) {

        return OrderItemEntity.builder()
                .itemId(orderItemRequest.getItemId())
                .name(orderItemRequest.getName())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .build();
    }

    private OrderResponse convertToResponse(OrderEntity order) {

    	 PaymentDetails paymentDetails = order.getPaymentDetails();

    	    // NULL GUARD — old orders before @Enumerated fix have null paymentDetails
    	    if (paymentDetails == null) {
    	        paymentDetails = PaymentDetails.builder()
    	                .status(PaymentDetails.PaymentStatus.PENDING)
    	                .build();
    	    }
    	
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .phoneNumber(order.getPhoneNumber())
                .subtotal(order.getSubtotal())
                .tax(order.getTax())
                .grandTotal(order.getGrandTotal())
                .paymentMethod(order.getPaymentMethod())
                .items(order.getItems() != null
                        ? order.getItems().stream()
                                .map(this::convertToItemResponse)
                                .collect(Collectors.toList())
                        : List.of())
                .paymentDetails(paymentDetails)
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderResponse.OrderItemResponse convertToItemResponse(
            OrderItemEntity orderItemEntity) {

        return OrderResponse.OrderItemResponse.builder()
                .itemId(orderItemEntity.getItemId())
                .name(orderItemEntity.getName())
                .price(orderItemEntity.getPrice())
                .quantity(orderItemEntity.getQuantity())
                .build();
    }

    // RAZORPAY SIGNATURE VERIFY
    private boolean verifyRazorpaySignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;

            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");

            javax.crypto.spec.SecretKeySpec secretKey =
                    new javax.crypto.spec.SecretKeySpec(
                            razorpayKeySecret.getBytes(
                                    java.nio.charset.StandardCharsets.UTF_8),
                            "HmacSHA256");

            mac.init(secretKey);

            byte[] hash = mac.doFinal(
                    payload.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            String generatedSignature =
                    org.apache.commons.codec.binary.Hex.encodeHexString(hash);

            return generatedSignature.equalsIgnoreCase(razorpaySignature);
            
        } catch (Exception e) {
        	log.error("Razorpay signature verification failed", e);
        	return false;
        }
    }


    @Override
    @Transactional
    public List<OrderResponse> findRecentOrders() {
        return orderEntityRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(5)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // MARK ORDER FAILED
    @Override
    @Transactional
    public void markOrderFailed(String orderId) {

        OrderEntity order = orderEntityRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        PaymentDetails paymentDetails = order.getPaymentDetails();

        if (paymentDetails.getStatus() != PaymentDetails.PaymentStatus.COMPLETED) {
            paymentDetails.setStatus(PaymentDetails.PaymentStatus.FAILED);
            order.setPaymentDetails(paymentDetails);
            orderEntityRepository.save(order);
        }
    }
    
    @Override
    public Double sumSalesByDate(LocalDate date) {
        Double total = orderEntityRepository.sumSalesByDate(date, PaymentStatus.COMPLETED);
        return total != null ? total : 0.0;
    }

    @Override
    public Long countByOrderDate(LocalDate date) {
        Long count = orderEntityRepository.countByOrderDate(date, PaymentStatus.COMPLETED);
        return count != null ? count : 0L;
    }

    @Override
    public Double sumSalesByDateAndCreatedBy(LocalDate date, String createdBy) {
        Double total = orderEntityRepository.sumSalesByDateAndCreatedBy(date, createdBy, PaymentStatus.COMPLETED);
        return total != null ? total : 0.0;
    }

    @Override
    public Long countByOrderDateAndCreatedBy(LocalDate date, String createdBy) {
        Long count = orderEntityRepository.countByOrderDateAndCreatedBy(date, createdBy, PaymentStatus.COMPLETED);
        return count != null ? count : 0L;
    }

    @Override
    public Long sumItemsSoldByDateAndCreatedBy(LocalDate date, String createdBy) {
        Long count = orderEntityRepository.sumItemsSoldByDateAndCreatedBy(date, createdBy, PaymentStatus.COMPLETED);
        return count != null ? count : 0L;
    }
    
    @Override
    @Transactional
    public List<OrderResponse> findRecentOrdersByUser(String createdBy) {
        return orderEntityRepository
                .findTop5ByCreatedByOrderByCreatedAtDesc(createdBy)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<OrderResponse> getOrdersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderEntityRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToResponse);
    }
    
}