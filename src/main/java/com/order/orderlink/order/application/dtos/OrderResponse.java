package com.order.orderlink.order.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class OrderResponse {

	public static class Orders {

	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Create {
		private final UUID orderId;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class GetOrders {
		private final List<OrderDTO> orders;
		private final int totalPages;
		private final int currentPage;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class GetOrderDetail {
		private final UUID orderId;
		private final String restaurantName;
		private final int totalPrice;
		private final String deliveryAddress;
		private final List<OrderFoodDTO> foods;
		private final String status;
		private final LocalDateTime createdAt;
		private final int paymentPrice;
		private final String paymentBank;
		private final String cardNumber;
		private final String paymentStatus;
	}

}
