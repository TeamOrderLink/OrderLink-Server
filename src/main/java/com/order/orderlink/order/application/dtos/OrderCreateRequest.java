package com.order.orderlink.order.application.dtos;

import java.util.List;
import java.util.UUID;

import com.order.orderlink.order.domain.OrderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
	private UUID restaurantId;
	private List<OrderFoodDTO> foods;
	private OrderType orderType;
	private Integer totalPrice;
	private String deliverAddress;
}
