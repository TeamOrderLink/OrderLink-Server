package com.order.orderlink.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiException {

	private int statusCode;
	private String errorMessage;

}
