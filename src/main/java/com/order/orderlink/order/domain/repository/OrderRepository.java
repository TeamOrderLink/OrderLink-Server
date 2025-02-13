package com.order.orderlink.order.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
