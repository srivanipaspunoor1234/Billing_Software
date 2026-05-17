package com.myproject.retail.billingsoftware.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myproject.retail.billingsoftware.io.PaymentDetails;
import com.myproject.retail.billingsoftware.io.PaymentMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
    private Long id;
    private String orderId;
    private String customerName;
    private String phoneNumber;
    private Double subtotal;
    private Double tax;
    private Double grandTotal;
    private String createdBy;
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();
    
    @Embedded
    private PaymentDetails paymentDetails;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
  
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
       
}