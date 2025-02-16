package com.order.orderlink.common.dtos;

import com.order.orderlink.common.enums.SuccessCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {
	private final int code;
	private final String message;
	private final T data;

	public static <T> SuccessResponse<T> success(SuccessCode successCode, T data) {
		return new SuccessResponse<>(successCode.getStatus().value(), successCode.getMessage(), data);
	}
}
