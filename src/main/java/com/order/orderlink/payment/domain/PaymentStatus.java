package com.order.orderlink.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
	COMPLETED("결제 완료"), CANCELED("결제 취소");

	private final String value;
}
