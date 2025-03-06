package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class RegionException extends BaseException {
	public RegionException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}
}
