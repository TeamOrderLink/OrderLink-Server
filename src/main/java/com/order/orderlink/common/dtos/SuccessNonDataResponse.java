package com.order.orderlink.common.dtos;

import com.order.orderlink.common.enums.SuccessCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessNonDataResponse {
	private final int code;
	private final String message;

	public static SuccessNonDataResponse success(SuccessCode successCode) {
		return new SuccessNonDataResponse(successCode.getStatus().value(), successCode.getMessage());
	}
}
