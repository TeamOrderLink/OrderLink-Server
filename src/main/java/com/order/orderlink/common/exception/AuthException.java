package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class AuthException extends BaseException {

	public AuthException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}

}
