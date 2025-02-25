package com.order.orderlink.order.application.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.OrderType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class OrderRequest {

	@Getter
	public static class Create {

		@NotNull
		private UUID restaurantId;

		private List<OrderFoodDTO> foods;

		@NotNull
		private int totalPrice;

		private String deliveryAddress;
		private String deliveryRequest;
		private OrderType orderType;
	}

	@Getter
	public static class UpdateStatus {
		private OrderStatus status;
	}

	@Getter
	public static class Search {
		private OrderStatus status;
		private String restaurantName;
		private String foodName;
		private LocalDate startDate;
		private LocalDate endDate;
	}
}
