package com.order.orderlink.order.domain.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {

	Page<Order> findAllByUserId(UUID userId, Pageable pageable);

	Page<Order> findAllByRestaurantId(UUID restaurantId, Pageable pageable);

	public Page<Order> searchOrdersWithItems(
		OrderStatus status, String restaurantName, String foodName, LocalDate startDate, LocalDate endDate,
		Pageable pageable
	);
}
