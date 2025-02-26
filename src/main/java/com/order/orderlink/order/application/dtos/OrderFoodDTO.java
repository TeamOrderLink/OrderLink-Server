package com.order.orderlink.order.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFoodDTO {
	private String foodName;
	private int price;
	private int count;
}
