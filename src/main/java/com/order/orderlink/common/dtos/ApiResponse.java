package com.order.orderlink.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

	private int code;
	private String message;
	private T data;

}
