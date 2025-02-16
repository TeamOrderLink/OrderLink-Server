package com.order.orderlink.orderitem.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.orderitem.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
