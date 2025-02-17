package com.order.orderlink.common.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공입니다."),
	USER_CREATE_SUCCESS(HttpStatus.OK, "회원가입 성공입니다."),
	USER_READ_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공입니다."),
	USER_UPDATE_SUCCESS(HttpStatus.OK, "회원 정보 수정 성공입니다."),
	USER_DELETE_SUCCESS(HttpStatus.OK, "회원 탈퇴 성공입니다."),
	ORDER_CREATE_SUCCESS(HttpStatus.OK, "주문 생성 성공입니다."),
	ORDER_GET_SUCCESS(HttpStatus.OK, "주문 목록 조회 성공입니다."),
	ORDER_GET_DETAIL_SUCCESS(HttpStatus.OK, "주문 상세 조회 성공입니다.");

	private final HttpStatus status;
	private final String message;
}

