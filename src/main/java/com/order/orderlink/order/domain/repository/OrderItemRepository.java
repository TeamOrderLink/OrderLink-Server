package com.order.orderlink.order.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.order.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
