package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class RestaurantException extends BaseException {
	public RestaurantException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
