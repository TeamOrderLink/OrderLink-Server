package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class UserException extends BaseException {

	public UserException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
