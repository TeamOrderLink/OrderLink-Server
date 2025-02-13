package com.order.orderlink.common.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestaurantClient {

	private final WebClient webClient;

}
