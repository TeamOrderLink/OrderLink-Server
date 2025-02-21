package com.order.orderlink.common.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// AUTH
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

	// USER
	USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	USER_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	USER_CONFIRM_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "새 비밀번호와 확인용 비밀번호가 일치하지 않습니다."),
	USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "사용자의 접근 권한이 존재하지 않습니다"),

	// ORDER
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
	ORDER_INVALID_STATUS(HttpStatus.BAD_REQUEST, "주문 상태가 올바르지 않습니다."),
	ORDER_SEARCH_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "검색 조건이 필요합니다."),

	// RESTAURANT
	RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "음식점을 찾을 수 없습니다."),

	// PAYMENT
	PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제내역이 존재하지 않습니다."),

	// ADDRESS
	ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배송지를 찾을 수 없거나 권한이 없습니다."),

	// CATEGORY
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다."),

	// REGION
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "운영지역을 찾을 수 없습니다."),
	REGION_NOT_PARENT(HttpStatus.BAD_REQUEST, "운영지역과 같은 상위지역으로 설정할 수 없습니다."),

	// FOOD
	FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "음식을 찾을 수 없습니다."),

	// AI
	AI_API_RESPONSE_PARSING_ERROR(HttpStatus.NOT_FOUND, "음식 설명 생성 도중 오류가 발생했습니다."),

	;

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
