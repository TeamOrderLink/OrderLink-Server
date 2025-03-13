package com.order.orderlink.order.exception;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.BaseException;

public class OrderException extends BaseException {
	public OrderException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
