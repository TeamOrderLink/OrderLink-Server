package com.order.orderlink.order.application;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.order.orderlink.common.exception.OrderException;
import com.order.orderlink.common.exception.RestaurantException;
import com.order.orderlink.order.application.dtos.OrderDTO;
import com.order.orderlink.order.application.dtos.OrderFoodDTO;
import com.order.orderlink.order.application.dtos.OrderRequest;
import com.order.orderlink.order.application.dtos.OrderResponse;
import com.order.orderlink.order.application.dtos.RestaurantOrderDTO;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.repository.OrderRepository;
import com.order.orderlink.orderitem.domain.OrderItem;
import com.order.orderlink.payment.domain.Payment;
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
		UUID userId = validateRoleAndGetUserId(userDetails, Arrays.asList(UserRoleEnum.CUSTOMER));

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Order> ordersPage = orderRepository.findAllByUserId(userId, pageable);

		List<OrderDTO> orders = new ArrayList<>();
		for (Order order : ordersPage.getContent()) {

			List<OrderFoodDTO> foods = getFoods(order);
			Restaurant restaurant = getRestaurant(order.getRestaurantId());

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

	private static List<OrderFoodDTO> getFoods(Order order) {
		List<OrderFoodDTO> foods = new ArrayList<>();
		for (OrderItem orderItem : order.getOrderItems()) {
			foods.add(new OrderFoodDTO(orderItem.getFoodName(), orderItem.getPrice(), orderItem.getQuantity()));
		}
		return foods;
	}

	private Restaurant getRestaurant(UUID restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new RestaurantException(ErrorCode.RESTAURANT_NOT_FOUND));
		return restaurant;
	}

	private static UUID validateRoleAndGetUserId(UserDetailsImpl userDetails, List<UserRoleEnum> roles) {
		if (!roles.contains(userDetails.getUser().getRole())) {
			throw new AuthException(ErrorCode.USER_ACCESS_DENIED);
		}
		UUID userId = getUserId(userDetails);
		return userId;
	}

	@Transactional(readOnly = true)
	public OrderResponse.GetOrderDetail getOrderDetail(UserDetailsImpl userDetails, UUID orderId) {
		UUID userId = validateRoleAndGetUserId(userDetails, Arrays.asList(UserRoleEnum.CUSTOMER, UserRoleEnum.OWNER));
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));

		List<OrderFoodDTO> foods = getFoods(order);
		Payment payment = order.getPayment();
		Restaurant restaurant = getRestaurant(order.getRestaurantId());

		return OrderResponse.GetOrderDetail.builder()
			.orderId(orderId)
			.restaurantName(restaurant.getName())
			.totalPrice(order.getTotalPrice())
			.deliveryAddress(order.getDeliveryAddress())
			.foods(foods)
			.status(order.getStatus().getValue())
			.createdAt(order.getCreatedAt())
			.paymentPrice(payment.getAmount())
			.paymentBank(payment.getBank())
			.cardNumber(maskCardNumber(payment.getCardNumber()))
			.paymentStatus(payment.getStatus().getValue())
			.build();
	}

	private String maskCardNumber(String cardNumber) {
		if (cardNumber != null && cardNumber.length() > 4) {
			// 카드 번호의 마지막 4자리 제외하고 '*'로 마스킹
			String last4Digits = cardNumber.substring(cardNumber.length() - 4);
			String masked = cardNumber.substring(0, cardNumber.length() - 4).replaceAll("[0-9]", "*");
			return masked + last4Digits;
		}
		return cardNumber;
	}

	public void updateOrderStatus(UserDetailsImpl userDetails, UUID orderId, OrderRequest.UpdateStatus request) {
		UUID userId = validateRoleAndGetUserId(userDetails, Arrays.asList(UserRoleEnum.OWNER, UserRoleEnum.CUSTOMER));
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));
		if (request.getStatus().equals(OrderStatus.CANCELED)) {
			order.updateOrderStatus(OrderStatus.CANCELED);
		} else {
			if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
				throw new AuthException(ErrorCode.USER_ACCESS_DENIED);
			}
			order.updateOrderStatus(request.getStatus());
		}
	}

	@Transactional(readOnly = true)
	public OrderResponse.GetRestaurantOrders getRestaurantOrders(UserDetailsImpl userDetails, UUID restaurantId,
		int page, int size) {
		UUID userId = validateRoleAndGetUserId(userDetails, Arrays.asList(UserRoleEnum.OWNER));
		Restaurant restaurant = getRestaurant(restaurantId);
		
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Order> ordersPage = orderRepository.findAllByRestaurantId(restaurantId, pageable);

		List<RestaurantOrderDTO> orders = new ArrayList<>();
		for (Order order : ordersPage.getContent()) {

			List<OrderFoodDTO> foods = getFoods(order);

			orders.add(RestaurantOrderDTO.builder()
				.orderId(order.getId())
				.userNickName(userDetails.getUser().getNickname())
				.foods(foods)
				.totalPrice(order.getTotalPrice())
				.deliveryAddress(order.getDeliveryAddress())
				.build());
		}
		return OrderResponse.GetRestaurantOrders.builder()
			.orders(orders)
			.totalPages(ordersPage.getTotalPages())
			.currentPage(page)
			.build();
	}
}

