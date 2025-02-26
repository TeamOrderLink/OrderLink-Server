package com.order.orderlink.common.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.orderlink.common.dtos.FoodRequest;

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

	public String createFood(FoodRequest.Create request) {
		return webClient.post()
			.uri(FOODS_URI)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(String.class)
			.block(); // 동기 호출
	}

}
