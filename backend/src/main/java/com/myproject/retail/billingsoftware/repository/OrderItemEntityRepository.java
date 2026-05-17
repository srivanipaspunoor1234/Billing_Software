package com.myproject.retail.billingsoftware.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.retail.billingsoftware.entity.OrderItemEntity;

public interface OrderItemEntityRepository extends JpaRepository<OrderItemEntity, Long>{

	
	
}
