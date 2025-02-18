package com.order.orderlink.common.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.orderlink.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserClient {

	private final WebClient webClient;

	private static final String USERS_URL = "/users";

	public User getUser(UUID userId, String accessToken) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder.path(USERS_URL + "/{userId}").build(userId))
			.header("Authorization", "Bearer " + accessToken)
			.retrieve()
			.bodyToMono(User.class)
			.block();
	}
}
