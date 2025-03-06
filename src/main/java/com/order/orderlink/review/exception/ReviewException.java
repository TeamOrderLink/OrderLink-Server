package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class ReviewException extends BaseException {

	public ReviewException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
