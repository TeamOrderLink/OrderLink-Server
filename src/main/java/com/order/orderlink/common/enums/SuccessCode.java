package com.order.orderlink.common.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

	USER_CREATE_SUCCESS(HttpStatus.OK, "회원가입이 완료되었습니다."),
	ORDER_CREATE_SUCCESS(HttpStatus.OK, "주문 생성 성공입니다."),
	ORDER_GET_SUCCESS(HttpStatus.OK, "주문 목록 조회 성공입니다.");

	private final HttpStatus status;
	private final String message;
}

