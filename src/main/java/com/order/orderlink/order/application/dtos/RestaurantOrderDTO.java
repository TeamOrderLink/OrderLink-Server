package com.order.orderlink.order.application.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantOrderDTO {
	private UUID orderId;
	private String userNickName;
	private int totalPrice;
	private String deliveryAddress;
	private List<OrderFoodDTO> foods;
}
