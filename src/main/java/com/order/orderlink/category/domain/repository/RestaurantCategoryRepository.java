package com.order.orderlink.category.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.category.domain.RestaurantCategory;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, UUID> {
	void deleteByCategoryId(UUID categoryId);
}
