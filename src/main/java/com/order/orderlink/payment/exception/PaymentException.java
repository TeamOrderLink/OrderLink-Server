package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class PaymentException extends BaseException {
	public PaymentException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
