package com.order.orderlink.order.application.dtos;

import java.util.List;
import java.util.UUID;

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
		private Integer totalPrice;

		private String deliverAddress;
		private String deliverRequest;
		private OrderType orderType;
	}

	public static class Read {

	}

	public static class Update {

	}

	public static class Delete {

	}

}
