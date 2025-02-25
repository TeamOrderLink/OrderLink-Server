package com.order.orderlink.common.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j(topic = "service-account-token-service")
@Service
public class ServiceAccountTokenService {

	@Value("${service.account.client-id}")
	private String clientId;

	@Value("${service.account.client-secret}")
	private String clientSecret;

	@Value("${service.account.token-uri}")
	private String tokenUri;

	private String cachedToken;
	private long tokenExpiryTime; // milliseconds

	public synchronized String getAccessToken() {
		// 캐시된 토큰이 없거나 만료되기 30초 전에 새 토큰을 요청 (갱신)
		if (cachedToken == null || System.currentTimeMillis() > (tokenExpiryTime - 30000)) {
			try {
				MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
				formData.add("grant_type", "client_credentials");
				formData.add("client_id", clientId);
				formData.add("client_secret", clientSecret);

				WebClient internalWebClient = WebClient.create(tokenUri);
				Mono<TokenResponse> tokenResponseMono = internalWebClient.post()
					.uri("")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(BodyInserters.fromFormData(formData))
					.retrieve()
					.bodyToMono(TokenResponse.class);

				TokenResponse tokenResponse = tokenResponseMono.block();
				if (tokenResponse != null) {
					cachedToken = tokenResponse.getAccessToken();
					tokenExpiryTime = System.currentTimeMillis() + tokenResponse.getExpiresIn() * 1000L;
				} else {
					throw new AuthException(ErrorCode.FAILED_TO_GET_SERVICE_ACCOUNT_TOKEN);
				}
			} catch (Exception e) {
				log.error("토큰 발급 실패: {}", e.getMessage(), e);
				throw new AuthException(ErrorCode.FAILED_TO_GET_SERVICE_ACCOUNT_TOKEN);
			}
		}
		return cachedToken;
	}

	@Getter
	@Setter
	public static class TokenResponse {
		private String access_token;
		private int expires_in;

		public String getAccessToken() {
			return access_token;
		}

		public int getExpiresIn() {
			return expires_in;
		}
	}

}
