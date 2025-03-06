package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class AddressException extends BaseException {

	public AddressException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
