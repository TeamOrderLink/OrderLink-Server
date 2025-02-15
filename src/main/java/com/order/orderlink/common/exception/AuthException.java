package com.order.orderlink.common.exception;

public class AuthException extends BaseException {

	public AuthException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
	
}
