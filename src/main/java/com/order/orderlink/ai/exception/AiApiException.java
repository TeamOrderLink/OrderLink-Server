package com.order.orderlink.ai.exception;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.BaseException;

public class AiApiException extends BaseException {

	public AiApiException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
