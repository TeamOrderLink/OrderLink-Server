package com.order.orderlink.deliverydetail.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryDetailResponse {

	@Getter
	@AllArgsConstructor
	public static class Create {
		private UUID id;
	}

	@Getter
	@AllArgsConstructor
	public static class Read {
		private UUID id;
		private String address;
		private String request;
		private Boolean isDefault;
		private LocalDateTime createdAt;
	}

	@Getter
	@AllArgsConstructor
	public static class Update {
		private UUID id;
		private String address;
		private String request;
		private Boolean isDefault;
	}

}
