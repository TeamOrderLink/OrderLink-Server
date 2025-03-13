package com.order.orderlink.user.exception;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.BaseException;

public class UserException extends BaseException {

	public UserException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
