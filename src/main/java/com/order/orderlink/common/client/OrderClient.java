package com.order.orderlink.common.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderClient {

	private final WebClient webClient;
	private static final String ORDERS_URL = "/orders";

	public String getOrders() {
		return webClient.get()
			.uri(ORDERS_URL)
			.retrieve()
			.bodyToMono(String.class)
			.block(); // 동기 호출
	}

}
