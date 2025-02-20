package com.order.orderlink.address.application.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class AddressRequest {

	@Getter
	public static class Create {
		@NotBlank(message = "배송 주소는 필수 입력 항목입니다.")
		private String address;
		private String request;
		private Boolean isDefault;
	}

	@Getter
	public static class Update {
		private String address;
		private String request;
		private Boolean isDefault;
	}

}
