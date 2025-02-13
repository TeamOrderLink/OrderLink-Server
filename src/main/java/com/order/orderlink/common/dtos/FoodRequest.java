package com.order.orderlink.common.dtos;

import lombok.Value;

public class FoodRequest {
	@Value
	public static class Create {
		String value;
	}
}
