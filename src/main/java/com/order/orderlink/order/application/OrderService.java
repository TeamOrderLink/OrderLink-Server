package com.order.orderlink.order.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.client.FoodClient;
import com.order.orderlink.order.application.dtos.OrderRequest;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.repository.OrderRepository;
import com.order.orderlink.orderitem.domain.OrderItem;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final FoodClient foodClient;
	private final OrderRepository orderRepository;

	@Transactional
	public void createOrder(OrderRequest.Create request) {
		// 주문 객체 생성
		Order order = Order.builder()
			.id(UUID.randomUUID())
			.restaurantId(request.getRestaurantId())
			.orderType(request.getOrderType())
			.totalPrice(request.getTotalPrice())
			.deliveryAddress(request.getDeliveryAddress())
			.deliveryRequest(request.getDeliveryRequest())
			.status(OrderStatus.NEW)
			.build();

		request.getFoods().forEach(food -> {
			OrderItem orderItem = OrderItem.builder()
				.id(UUID.randomUUID())
				.order(order)
				.foodName(food.getFoodName())
				.quantity(food.getFoodCount())
				.price(food.getFoodPrice())
				.build();
			order.addOrderItem(orderItem);
		});

		orderRepository.save(order);
	}
}
