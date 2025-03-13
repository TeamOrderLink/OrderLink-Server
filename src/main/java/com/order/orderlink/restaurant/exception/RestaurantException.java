package com.order.orderlink.restaurant.exception;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.BaseException;

public class RestaurantException extends BaseException {
	public RestaurantException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
