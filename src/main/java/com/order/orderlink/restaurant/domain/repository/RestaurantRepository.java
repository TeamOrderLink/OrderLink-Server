package com.order.orderlink.restaurant.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.restaurant.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

}
