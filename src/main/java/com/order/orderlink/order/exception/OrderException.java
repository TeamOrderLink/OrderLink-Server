package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class OrderException extends BaseException {
	public OrderException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
