package com.order.orderlink.common.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
	ORDER_CREATE_SUCCESS(HttpStatus.OK, "주문 생성 성공입니다.");

	private final HttpStatus status;
	private final String message;
}

