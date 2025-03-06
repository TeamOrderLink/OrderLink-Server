package com.order.orderlink.common.exception;

import com.order.orderlink.common.enums.ErrorCode;

public class FoodException extends BaseException {

    public FoodException(ErrorCode errorCode) {
        super(errorCode.getStatus(), errorCode.getMessage());
    }
}
