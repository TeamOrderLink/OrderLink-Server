package com.order.orderlink.common.dtos;

import com.order.orderlink.common.enums.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse<T> {
	private final int code;
	private final String message;
	private final T data;

	public static <T> ErrorResponse<T> error(ErrorCode errorCode, T data) {
		return new ErrorResponse<>(errorCode.getStatus().value(), errorCode.getMessage(), data);
	}
}