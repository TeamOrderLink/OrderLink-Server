package com.order.orderlink.food.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.food.domain.Food;

public interface FoodRepository extends JpaRepository<Food, UUID> {

	Page<Food> findByRestaurantId(UUID restaurantId, Pageable pageable);
}
