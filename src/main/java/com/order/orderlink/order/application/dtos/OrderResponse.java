package com.order.orderlink.order.application.dtos;

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
}
