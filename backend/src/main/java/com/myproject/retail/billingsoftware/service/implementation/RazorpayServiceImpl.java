package com.myproject.retail.billingsoftware.service.implementation;

//import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//import com.myproject.retail.billingsoftware.entity.OrderEntity;
//import com.myproject.retail.billingsoftware.io.PaymentDetails;
//import com.myproject.retail.billingsoftware.io.PaymentMethod;
import com.myproject.retail.billingsoftware.io.RazorpayOrderResponse;
import com.myproject.retail.billingsoftware.repository.OrderEntityRepository;
import com.myproject.retail.billingsoftware.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorpayServiceImpl implements RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

//    @Autowired
//    private OrderEntityRepository orderEntityRepository;

    @Override
    public RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException {

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        long amountInPaise = Math.round(amount * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", currency != null ? currency : "INR");
        orderRequest.put("receipt", "order_rcptid_" + System.currentTimeMillis());
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);

        return convertToResponse(order);
    }

    private RazorpayOrderResponse convertToResponse(Order order) {

        JSONObject json = order.toJson();

        Long createdAt = json.optLong("created_at", 0L);

        String formatted = java.time.Instant.ofEpochSecond(createdAt)
                .atZone(java.time.ZoneId.systemDefault())
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return RazorpayOrderResponse.builder()
                .id(json.optString("id"))
                .entity(json.optString("entity"))
                .amount(json.optLong("amount"))
                .currency(json.optString("currency"))
                .status(json.optString("status"))
                .receipt(json.optString("receipt"))
                .createdAt(createdAt)
                .createdAtFormatted(formatted)
                .build();
    }
}