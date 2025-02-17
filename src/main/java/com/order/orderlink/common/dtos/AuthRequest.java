package com.order.orderlink.common.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class AuthRequest {

	@Getter
	public static class Login {

		@NotBlank(message = "아이디는 필수 항목입니다.")
		private String username;

		@NotBlank(message = "비밀번호는 필수 항목입니다.")
		private String password;
	}

}
