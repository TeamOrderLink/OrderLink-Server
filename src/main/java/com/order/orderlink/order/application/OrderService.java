package com.order.orderlink.order.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.client.RestaurantClient;
import com.order.orderlink.common.client.UserClient;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.common.exception.OrderException;
import com.order.orderlink.order.application.dtos.OrderDTO;
import com.order.orderlink.order.application.dtos.OrderFoodDTO;
import com.order.orderlink.order.application.dtos.OrderRequest;
import com.order.orderlink.order.application.dtos.OrderResponse;
import com.order.orderlink.order.application.dtos.RestaurantOrderDTO;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.repository.OrderRepository;
import com.order.orderlink.order.domain.repository.OrderRepositoryImpl;
import com.order.orderlink.orderitem.domain.OrderItem;
import com.order.orderlink.payment.domain.Payment;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.UserRoleEnum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserClient userClient;
	private final OrderRepositoryImpl orderRepositoryImpl;
	private final RestaurantClient restaurantClient;

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
		UUID userId = getUserId(userDetails);

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Order> ordersPage = orderRepository.findAllByUserId(userId, pageable);

		List<OrderDTO> orders = getOrderDTOS(ordersPage);
		return OrderResponse.GetOrders.builder()
			.orders(orders)
			.totalPages(ordersPage.getTotalPages())
			.currentPage(page)
			.build();
	}

	private List<OrderDTO> getOrderDTOS(Page<Order> searchedOrdersPage) {
		List<OrderDTO> searchedOrders = new ArrayList<>();
		for (Order order : searchedOrdersPage.getContent()) {

			List<OrderFoodDTO> foods = getFoods(order);
			Restaurant restaurant = getRestaurant(order.getRestaurantId());

			searchedOrders.add(OrderDTO.builder()
				.paymentId(order.getPayment().getId())
				.orderId(order.getId())
				.restaurantName(restaurant.getName())
				.foods(foods)
				.totalPrice(order.getTotalPrice())
				.deliveryAddress(order.getDeliveryAddress())
				.build());
		}
		return searchedOrders;
	}

	private static List<OrderFoodDTO> getFoods(Order order) {
		List<OrderFoodDTO> foods = new ArrayList<>();
		for (OrderItem orderItem : order.getOrderItems()) {
			foods.add(new OrderFoodDTO(orderItem.getFoodName(), orderItem.getPrice(), orderItem.getQuantity()));
		}
		return foods;
	}

	private Restaurant getRestaurant(UUID restaurantId) {
		Restaurant restaurant = restaurantClient.getRestaurant(restaurantId);
		return restaurant;
	}

	@Transactional(readOnly = true)
	public OrderResponse.GetOrderDetail getOrderDetail(UserDetailsImpl userDetails, UUID orderId) {
		UUID userId = getUserId(userDetails);
		Order order = getOrderById(orderId);

		List<OrderFoodDTO> foods = getFoods(order);
		Payment payment = order.getPayment();
		Restaurant restaurant = getRestaurant(order.getRestaurantId());

		Result paymentNullCheck = getPaymentNullCheck(payment);

		return OrderResponse.GetOrderDetail.builder()
			.orderId(orderId)
			.restaurantName(restaurant.getName())
			.totalPrice(order.getTotalPrice())
			.deliveryAddress(order.getDeliveryAddress())
			.foods(foods)
			.status(order.getStatus().getValue())
			.createdAt(order.getCreatedAt())
			.paymentPrice(paymentNullCheck.paymentPrice())
			.paymentBank(paymentNullCheck.paymentBank())
			.cardNumber(paymentNullCheck.cardNumber())
			.paymentStatus(paymentNullCheck.paymentStatus())
			.build();
	}

	private Result getPaymentNullCheck(Payment payment) {
		int paymentPrice = (payment != null) ? payment.getAmount() : 0;
		String paymentBank = (payment != null) ? payment.getBank() : "N/A";
		String cardNumber = (payment != null) ? maskCardNumber(payment.getCardNumber()) : "N/A";
		String paymentStatus = (payment != null) ? payment.getStatus().getValue() : "N/A";
		Result paymentNullCheck = new Result(paymentPrice, paymentBank, cardNumber, paymentStatus);
		return paymentNullCheck;
	}

	private record Result(int paymentPrice, String paymentBank, String cardNumber, String paymentStatus) {
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
		UUID userId = getUserId(userDetails);
		Order order = getOrderById(orderId);
		if (request.getStatus().equals(OrderStatus.CANCELED)) {
			//주문 취소는 5분 내에만
			if (Duration.between(order.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5) {
				throw new OrderException(ErrorCode.ORDER_CANCEL_TIME_EXCEEDED);
			}
			order.updateOrderStatus(OrderStatus.CANCELED);
		} else {
			//customer 는 주문 취소만 할 수 있음
			if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER)) {
				throw new AuthException(ErrorCode.USER_ACCESS_DENIED);
			}
			order.updateOrderStatus(request.getStatus());
		}
	}

	@Transactional(readOnly = true)
	public OrderResponse.GetRestaurantOrders getRestaurantOrders(UserDetailsImpl userDetails, UUID restaurantId,
		int page, int size, HttpServletRequest httpServletRequest) {
		UUID userId = getUserId(userDetails);
		Restaurant restaurant = getRestaurant(restaurantId);
		String accessToken = httpServletRequest.getHeader("Authorization");

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Order> ordersPage = orderRepository.findAllByRestaurantId(restaurantId, pageable);

		List<RestaurantOrderDTO> orders = new ArrayList<>();
		for (Order order : ordersPage.getContent()) {

			List<OrderFoodDTO> foods = getFoods(order);
			User user = userClient.getUser(order.getUserId(), accessToken);

			orders.add(RestaurantOrderDTO.builder()
				.paymentId(order.getPayment().getId())
				.orderId(order.getId())
				.userNickName(user.getNickname())
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

	@Transactional(readOnly = true)
	public Order getOrderById(UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));
		return order;
	}

	@Transactional(readOnly = true)
	public OrderResponse.GetOrders getSearchedOrders(UserDetailsImpl userDetails, OrderRequest.Search request, int page,
		int size) {
		UUID userId = getUserId(userDetails);
		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}
		Sort sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("updatedAt"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		Page<Order> searchedOrdersPage = orderRepositoryImpl.searchOrdersWithItems(request.getStatus(),
			request.getRestaurantName(), request.getFoodName(), request.getStartDate(), request.getEndDate(), pageable);

		List<OrderDTO> searchedOrders = getOrderDTOS(searchedOrdersPage);
		return OrderResponse.GetOrders.builder()
			.orders(searchedOrders)
			.totalPages(searchedOrdersPage.getTotalPages())
			.currentPage(page)
			.build();

	}
}

