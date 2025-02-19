package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class DeliveryDetailException extends BaseException {

	public DeliveryDetailException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
