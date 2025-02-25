package com.order.orderlink.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
	NEW("주문 등록"), APPROVED("주문 승인"), CANCELED("주문 취소"), DELIVERY("배달 중");

	private final String value;
}
