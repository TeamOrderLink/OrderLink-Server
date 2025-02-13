package com.order.orderlink.food.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.food.domain.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {

}
