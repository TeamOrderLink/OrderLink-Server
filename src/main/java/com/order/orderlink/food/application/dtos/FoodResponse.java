package com.order.orderlink.food.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class FoodResponse {

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Create {
		private final UUID id;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class Update {
		private final UUID id;
		private final String name;
		private final String description;
		private final int price;
		private final boolean isHidden;
		private final LocalDateTime updatedAt;
	}

	@Getter
	@AllArgsConstructor
	public static class Delete {
		private final UUID id;
		private final LocalDateTime deletedAt;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class UploadImage {
		private final String imageUrl;
	}

	@Getter
	@AllArgsConstructor
	public static class GetFoods {
		List<FoodDTO> foods;
		int totalPages;
		int currentPage;

	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class GetFoodDetail {
		private String name;
		private String description;
		private int price;
		private String imageUrl;
		private boolean isHidden;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}

}
