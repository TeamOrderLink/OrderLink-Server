package com.order.orderlink.common.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserClient {

	private final WebClient webClient;

	private static final String USERS_URL = "/users";

	public boolean isRestaurantExists(UUID userId) {
		try {
			webClient.get()
				.uri(uriBuilder -> uriBuilder.path(USERS_URL + "/{userId}").build(userId))
				.retrieve()
				.toBodilessEntity()
				.block();

			return true;
		} catch (WebClientResponseException.NotFound e) {
			return false;
		}
	}

}
