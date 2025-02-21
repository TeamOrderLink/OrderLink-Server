package com.order.orderlink.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiWebClientConfig {

	@Bean
	@Qualifier("aiWebClient")
	public WebClient aiWebClient(WebClient.Builder builder, @Value("${ai.api.base-url}") String aiBaseUrl) {
		return builder
			.baseUrl(aiBaseUrl) // 기본 URL 설정
			.defaultHeader("Content-Type", "application/json") // 기본 헤더 설정
			.build();
	}
}
