package com.order.orderlink.category.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.category.domain.RestaurantCategory;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, UUID> {
	void deleteByCategoryId(UUID categoryId);

	List<RestaurantCategory> findAllByCategoryId(UUID categoryId);
}
