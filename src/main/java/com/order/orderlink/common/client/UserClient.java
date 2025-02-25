package com.order.orderlink.common.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.orderlink.common.auth.service.ServiceAccountTokenService;
import com.order.orderlink.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserClient {

	private final WebClient webClient;
	private final ServiceAccountTokenService tokenService;

	private static final String USERS_URL = "/users";

	public User getUser(UUID userId, String accessToken) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder.path(USERS_URL + "/{userId}/getUser").build(userId))
			.header("Authorization", accessToken)
			.retrieve()
			.bodyToMono(User.class)
			.block();
	}

	// 인증이 되지 않은 상태에서 내부 서비스 호출을 위한 메서드
	public User getUser(UUID userId) {
		String accessToken = getAccessTokenForInternalCall();
		return webClient.get()
			.uri(uriBuilder -> uriBuilder.path(USERS_URL + "/{userId}/getUser").build(userId))
			.header("Authorization", accessToken)
			.retrieve()
			.bodyToMono(User.class)
			.block();
	}

	private String getAccessTokenForInternalCall() {
		return tokenService.getAccessToken();
	}

}
