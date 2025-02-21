package com.order.orderlink.food.application.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FoodDTO {
	private UUID foodId;
	private String name;
	private int price;
	private String imageUrl;
	private boolean isHidden;
}
