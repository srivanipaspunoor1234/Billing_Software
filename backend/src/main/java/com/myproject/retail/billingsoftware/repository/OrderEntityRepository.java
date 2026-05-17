package com.myproject.retail.billingsoftware.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.myproject.retail.billingsoftware.entity.OrderEntity;
import com.myproject.retail.billingsoftware.io.PaymentDetails.PaymentStatus;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

	Page<OrderEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
	
    Optional<OrderEntity> findByOrderId(String orderId);

    List<OrderEntity> findAllByOrderByCreatedAtDesc();

    List<OrderEntity> findTop5ByOrderByCreatedAtDesc();

    List<OrderEntity> findTop5ByCreatedByOrderByCreatedAtDesc(String createdBy);

    @Query("SELECT o FROM OrderEntity o ORDER BY o.createdAt DESC")
    List<OrderEntity> findRecentOrders(Pageable pageable);

    // Pass PaymentStatus.COMPLETED as a parameter — avoids JPQL enum parsing issues
    @Query("SELECT SUM(o.grandTotal) FROM OrderEntity o WHERE DATE(o.createdAt) = :date AND o.paymentDetails.status = :status")
    Double sumSalesByDate(@Param("date") LocalDate date, @Param("status") PaymentStatus status);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE DATE(o.createdAt) = :date AND o.paymentDetails.status = :status")
    Long countByOrderDate(@Param("date") LocalDate date, @Param("status") PaymentStatus status);

    @Query("SELECT SUM(o.grandTotal) FROM OrderEntity o WHERE DATE(o.createdAt) = :date AND o.createdBy = :createdBy AND o.paymentDetails.status = :status")
    Double sumSalesByDateAndCreatedBy(@Param("date") LocalDate date, @Param("createdBy") String createdBy, @Param("status") PaymentStatus status);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE DATE(o.createdAt) = :date AND o.createdBy = :createdBy AND o.paymentDetails.status = :status")
    Long countByOrderDateAndCreatedBy(@Param("date") LocalDate date, @Param("createdBy") String createdBy, @Param("status") PaymentStatus status);

    @Query("SELECT SUM(oi.quantity) FROM OrderItemEntity oi WHERE DATE(oi.order.createdAt) = :date AND oi.order.createdBy = :createdBy AND oi.order.paymentDetails.status = :status")
    Long sumItemsSoldByDateAndCreatedBy(@Param("date") LocalDate date, @Param("createdBy") String createdBy, @Param("status") PaymentStatus status);
}