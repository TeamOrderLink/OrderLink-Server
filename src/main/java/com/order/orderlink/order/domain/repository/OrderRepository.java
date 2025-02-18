package com.order.orderlink.order.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.order.domain.Order;

import jakarta.validation.constraints.NotNull;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	Page<Order> findAllByUserId(UUID userId, Pageable pageable);

	Page<Order> findAllByRestaurantId(UUID restaurantId, Pageable pageable);

	Optional<Object> findByUserId(@NotNull UUID userId);
}
