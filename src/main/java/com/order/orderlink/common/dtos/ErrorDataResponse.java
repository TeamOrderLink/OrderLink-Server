package com.order.orderlink.common.dtos;

import com.order.orderlink.common.enums.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDataResponse<T> {
	private final int code;
	private final String message;
	private final T data;

	public static <T> ErrorDataResponse<T> error(ErrorCode errorCode, T data) {
		return new ErrorDataResponse<>(errorCode.getStatus().value(), errorCode.getMessage(), data);
	}
}