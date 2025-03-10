package com.order.orderlink.payment.application.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentRequest {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
