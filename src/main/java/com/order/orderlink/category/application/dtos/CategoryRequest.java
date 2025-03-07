package com.order.orderlink.category.application.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CategoryRequest {

	@Getter
	public static class Create {

		@NotBlank
		private String name;

		private Create() {
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class RegisterRestaurantCategory {
		@NotEmpty
		private List<UUID> categoryIds;
	}

	@Getter
	public static class UpdateCategory {
		@NotBlank
		private String name;

		private UpdateCategory() {
		}
	}
}
