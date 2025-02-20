package com.order.orderlink.address.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {

	@Getter
	@AllArgsConstructor
	public static class Create {
		private UUID id;
	}

	@Getter
	@AllArgsConstructor
	public static class ReadAll {
		private List<AddressResponse.Read> addresses;
		private int currentPage;
		private int totalPages;
		private long totalElements;
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
