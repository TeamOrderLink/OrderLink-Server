package com.order.orderlink.payment.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PaymentResponse {
	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {
		private final UUID paymentId;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class GetPayment {
		private String cardHolder;
		private String bank;
		private String cardNumber;
		private int amount;
		private String status;
		private LocalDateTime createdAt;
	}
}
