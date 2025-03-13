package com.order.orderlink.address.application.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressRequest {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {
		@NotBlank(message = "배송 주소는 필수 입력 항목입니다.")
		private String address;
		private Boolean isDefault;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Update {
		private String address;
		private Boolean isDefault;
	}

}
