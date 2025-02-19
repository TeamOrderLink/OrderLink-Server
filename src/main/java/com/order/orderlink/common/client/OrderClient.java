package com.order.orderlink.common.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.orderlink.order.domain.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderClient {

	private final WebClient webClient;
	private static final String ORDERS_URL = "/orders";

	public Order getOrder(UUID orderId, String accessToken) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder.path(ORDERS_URL + "/{orderId}/getOrder").build(orderId))
			.header("Authorization", accessToken)
			.retrieve()
			.bodyToMono(Order.class)
			.block();
	}

}
