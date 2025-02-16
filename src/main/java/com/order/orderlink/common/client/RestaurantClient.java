package com.order.orderlink.common.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestaurantClient {

	private final WebClient webClient;
	private static final String RESTAURANTS_URL = "/restaurants";

	public boolean isRestaurantExists(UUID restaurantId) {
		try {
			webClient.get()
				.uri(uriBuilder -> uriBuilder.path(RESTAURANTS_URL + "/{restaurantId}").build(restaurantId))
				.retrieve()
				.toBodilessEntity()
				.block();

			return true;
		} catch (WebClientResponseException.NotFound e) {
			return false;
		}
	}
}
