package com.order.orderlink.common.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공입니다."),
	USER_CREATE_SUCCESS(HttpStatus.OK, "회원 가입 성공입니다."),
	USER_READ_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공입니다."),
	USER_UPDATE_SUCCESS(HttpStatus.OK, "회원 정보 수정 성공입니다."),
	USER_PASSWORD_UPDATE_SUCCESS(HttpStatus.OK, "비밀번호 변경 성공입니다."),
	USER_DELETE_SUCCESS(HttpStatus.OK, "회원 삭제 성공입니다."),
	ORDER_CREATE_SUCCESS(HttpStatus.OK, "주문 생성 성공입니다."),
	ORDER_GET_SUCCESS(HttpStatus.OK, "주문 목록 조회 성공입니다."),
	ORDER_GET_DETAIL_SUCCESS(HttpStatus.OK, "주문 상세 조회 성공입니다."),
	ORDER_UPDATE_STATUS_SUCCESS(HttpStatus.OK, "주문 상태 변경 성공입니다."),
	ORDERS_OWNER_GET_SUCESS(HttpStatus.OK, "음식점 주문 리스트 조회 성공입니다"),
    FOOD_CREATE_SUCCESS(HttpStatus.OK, "음식 등록 성공입니다.");

	private final HttpStatus status;
	private final String message;
}

