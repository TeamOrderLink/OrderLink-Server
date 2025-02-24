package com.order.orderlink.category.domain.repository;

import com.order.orderlink.category.domain.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, UUID> {
	void deleteByCategoryId(UUID categoryId);

	List<RestaurantCategory> findAllByCategoryId(UUID categoryId);
}
