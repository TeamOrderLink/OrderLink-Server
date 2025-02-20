package com.order.orderlink.food.domain.repository;

import com.order.orderlink.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {

}
