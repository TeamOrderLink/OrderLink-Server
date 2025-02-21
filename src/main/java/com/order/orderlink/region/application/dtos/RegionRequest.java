package com.order.orderlink.region.application.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class RegionRequest {
	@Getter
	public static class Create {
		@NotBlank
		private String regionName;
		private UUID parentId;
	}

	@Getter
	public static class Update {
		@NotBlank
		private String regionName;
		private UUID parentId;
	}
}
