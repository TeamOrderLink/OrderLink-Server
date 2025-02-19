package com.order.orderlink.payment.application.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PaymentResponse {
	@Getter
	@Builder
	@AllArgsConstructor
	public static class Create {
		private final UUID paymentId;
	}
}
