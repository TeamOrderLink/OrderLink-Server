package com.order.orderlink.order.application.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.OrderType;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderRequest {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {

		@NotNull
		private UUID restaurantId;

		private List<OrderItemDTO> foods;

		@NotNull
		private int totalPrice;

		private String deliveryAddress;
		private String deliveryRequest;
		private OrderType orderType;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class UpdateStatus {
		private OrderStatus status;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Search {
		private OrderStatus status;
		private String restaurantName;
		private String foodName;
		private LocalDate startDate;
		private LocalDate endDate;
	}
}
