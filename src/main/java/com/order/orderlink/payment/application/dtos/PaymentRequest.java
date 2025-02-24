package com.order.orderlink.payment.application.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class PaymentRequest {
	@Getter
	@Builder
	public static class Create {

		@NotNull
		private UUID orderId;

		@NotBlank
		private String cardNumber;

		@NotBlank
		private String bank;

		@NotBlank
		private String cardHolder;

		@NotBlank
		private String expiryDate;
		private int amount;
	}
}
