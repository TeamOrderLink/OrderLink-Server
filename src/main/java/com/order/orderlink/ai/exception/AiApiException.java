package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class AiApiException extends BaseException {

	public AiApiException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
