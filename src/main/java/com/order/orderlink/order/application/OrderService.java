package com.order.orderlink.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.common.exception.RestaurantException;
import com.order.orderlink.order.application.dtos.OrderDTO;
import com.order.orderlink.order.application.dtos.OrderFoodDTO;
import com.order.orderlink.order.application.dtos.OrderRequest;
import com.order.orderlink.order.application.dtos.OrderResponse;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.repository.OrderRepository;
import com.order.orderlink.orderitem.domain.OrderItem;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;
import com.order.orderlink.user.domain.UserRoleEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final RestaurantRepository restaurantRepository;

	public OrderResponse.Create createOrder(UserDetailsImpl userDetails, OrderRequest.Create request) {
		UUID userId = getUserId(userDetails);
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

	private static UUID getUserId(UserDetailsImpl userDetails) {
		UUID userId = userDetails.getUser().getId();
		return userId;
	}

	@Transactional(readOnly = true)
	public OrderResponse.GetOrders getMyOrders(UserDetailsImpl userDetails, int page, int size) {
		UUID userId = validateRoleAndGetUserId(userDetails, UserRoleEnum.CUSTOMER);

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Order> ordersPage = orderRepository.findAllByUserId(userId, pageable);

		List<OrderDTO> orders = new ArrayList<>();
		for (Order order : ordersPage.getContent()) {
			List<OrderFoodDTO> foods = new ArrayList<>();
			for (OrderItem food : order.getOrderItems()) {
				foods.add(new OrderFoodDTO(food.getFoodName(), food.getPrice(), food.getQuantity()));
			}
			Restaurant restaurant = restaurantRepository.findById(order.getRestaurantId())
				.orElseThrow(() -> new RestaurantException(ErrorCode.RESTAURANT_NOT_FOUND));

			orders.add(OrderDTO.builder()
				.orderId(order.getId())
				.restaurantName(restaurant.getName())
				.foods(foods)
				.totalPrice(order.getTotalPrice())
				.deliveryAddress(order.getDeliveryAddress())
				.build());
		}
		return OrderResponse.GetOrders.builder()
			.orders(orders)
			.totalPages(ordersPage.getTotalPages())
			.currentPage(page)
			.build();
	}

	private static UUID validateRoleAndGetUserId(UserDetailsImpl userDetails, UserRoleEnum role) {
		if (!userDetails.getUser().getRole().equals(role)) {
			throw new AuthException(ErrorCode.USER_ACCESS_DENIED);
		}
		UUID userId = getUserId(userDetails);
		return userId;
	}
}

