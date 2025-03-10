package com.order.orderlink.address.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {
		private UUID id;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ReadAll {
		private List<AddressResponse.Read> addresses;
		private int currentPage;
		private int totalPages;
		private long totalElements;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Read {
		private UUID id;
		private String address;
		private Boolean isDefault;
		private LocalDateTime createdAt;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Update {
		private UUID id;
		private String address;
		private Boolean isDefault;
	}

}
