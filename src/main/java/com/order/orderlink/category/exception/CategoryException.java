package com.order.orderlink.category.exception;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.BaseException;

public class CategoryException extends BaseException {
	public CategoryException(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getMessage());
	}

	public static class CategoryNotFoundException extends CategoryException {
		public CategoryNotFoundException() {
			super(ErrorCode.CATEGORY_NOT_FOUND);
		}
	}
}
