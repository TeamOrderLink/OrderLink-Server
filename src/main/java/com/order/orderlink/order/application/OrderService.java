package com.order.orderlink.order.application;

import org.springframework.stereotype.Service;

import com.order.orderlink.common.client.FoodClient;
import com.order.orderlink.order.application.dtos.OrderRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final FoodClient foodClient;

	public String createOrder(OrderRequest.Create request) {
		System.out.println("request = " + request.getValue());
		System.out.println("오더 생성을 위해 상품을 땡겨와 봅시다 !! :) ~ " + foodClient.getFoods());
		return "createOrder() has been called!";
	}

}
