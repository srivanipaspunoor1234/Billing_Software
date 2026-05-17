package com.myproject.retail.billingsoftware.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.retail.billingsoftware.entity.ItemEntity;


public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

	Optional<ItemEntity> findByItemId(String id);

	Long countByCategory_Id(Long id);
	
}
