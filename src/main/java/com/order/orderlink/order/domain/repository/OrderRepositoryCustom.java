package com.order.orderlink.order.domain.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;

public interface OrderRepositoryCustom {
	Page<Order> searchOrdersWithItems(OrderStatus status, String restaurantName, String foodName,
		LocalDate startDate, LocalDate endDate, Pageable pageable);
}
