package com.order.orderlink.common.dtos;

import org.springframework.http.HttpStatus;

import com.order.orderlink.common.enums.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorNonDataResponse {
	private final int code;
	private final String message;

	public static ErrorNonDataResponse error(ErrorCode errorCode) {
		return new ErrorNonDataResponse(errorCode.getStatus().value(), errorCode.getMessage());
	}

	public static ErrorNonDataResponse badRequestError(final String errorMessage) {
		return new ErrorNonDataResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
	}
}

