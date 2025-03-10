package com.order.orderlink.order.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.orderlink.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, OrderCustomRepository {

	Page<Order> findAllByUserId(UUID userId, Pageable pageable);

	Page<Order> findAllByRestaurantId(UUID restaurantId, Pageable pageable);
}
