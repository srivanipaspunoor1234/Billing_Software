package com.myproject.retail.billingsoftware.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.retail.billingsoftware.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{

	
	Optional<CategoryEntity> findByCategoryId(String categoryId);
	
}
