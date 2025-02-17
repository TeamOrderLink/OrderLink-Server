package com.order.orderlink.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponse {

	@Getter
	@AllArgsConstructor
	public static class Login {
		private String token;
	}

}
