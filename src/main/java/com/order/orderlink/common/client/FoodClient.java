package com.order.orderlink.common.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.orderlink.food.domain.Food;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FoodClient {

	private final WebClient webClient;
	private static final String FOODS_URI = "/foods";

	public String getFoods() {
		return webClient.get()
			.uri(FOODS_URI)
			.retrieve()
			.bodyToMono(String.class)
			.block(); // 동기 호출
	}

	public Food getFood(UUID foodId) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder.path(FOODS_URI + "/{foodId}/getFood").build(foodId))
			.retrieve()
			.bodyToMono(Food.class)
			.block();
	}
}
