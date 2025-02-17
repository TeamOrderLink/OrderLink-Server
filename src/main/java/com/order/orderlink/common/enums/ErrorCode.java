package com.order.orderlink.common.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
	RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "음식점을 찾을 수 없습니다."),
	ORDER_INVALID_STATUS(HttpStatus.BAD_REQUEST, "주문 상태가 올바르지 않습니다."),
	USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "사용자의 접근 권한이 존재하지 않습니다");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
