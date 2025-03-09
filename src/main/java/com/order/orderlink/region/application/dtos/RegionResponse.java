package com.order.orderlink.region.application.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class RegionResponse {
	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {
		private final UUID regionId;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class GetRegions {
		private final List<RegionDTO> regions;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class GetTreeRegions {
		private final UUID regionId;
		private final String name;
		private final UUID parentId;
		private final List<GetTreeRegions> regions;
	}
}
