package com.order.orderlink.common.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.orderlink.restaurant.domain.Restaurant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestaurantClient {

	private final WebClient webClient;
	private static final String RESTAURANTS_URL = "/restaurants";

	public Restaurant getRestaurant(UUID restaurantId) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder.path(RESTAURANTS_URL + "/{restaurantId}/getRestaurant").build(restaurantId))
			.retrieve()
			.bodyToMono(Restaurant.class)
			.block();

	}
}
