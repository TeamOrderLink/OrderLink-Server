package com.order.orderlink.order.application;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.order.application.dtos.OrderRequest;
import com.order.orderlink.order.application.dtos.OrderResponse;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.repository.OrderRepository;
import com.order.orderlink.orderitem.domain.OrderItem;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final EntityManager entityManager; // Flush를 위해 추가

	public OrderResponse.Create createOrder(UUID userId, OrderRequest.Create request) {
		Order order = Order.builder()
			.userId(userId)
			.restaurantId(request.getRestaurantId())
			.deliveryAddress(request.getDeliveryAddress())
			.deliveryRequest(request.getDeliveryRequest())
			.totalPrice(request.getTotalPrice())
			.status(OrderStatus.NEW)
			.orderType(request.getOrderType())
			.orderItems(new ArrayList<>())
			.build();

		request.getFoods().forEach(food -> order.addOrderItem(
			OrderItem.builder()
				.order(order)
				.foodName(food.getFoodName())
				.price(food.getPrice())
				.quantity(food.getCount())
				.build()
		));

		orderRepository.save(order);

		return new OrderResponse.Create(order.getId());
	}
}

