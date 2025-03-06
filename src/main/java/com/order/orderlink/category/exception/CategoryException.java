package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class CategoryException extends BaseException {
	public CategoryException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
