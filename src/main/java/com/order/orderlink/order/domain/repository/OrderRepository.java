package com.order.orderlink.order.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	Page<Order> findAllByUserId(UUID userId, Pageable pageable);
}
