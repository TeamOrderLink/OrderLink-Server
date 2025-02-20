package com.order.orderlink.category.application.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class CategoryRequest {

	@Getter
	public static class Create {

		@NotBlank
		private String name;
	}

	@Getter
	public static class RegisterRestaurantCategory {
		@NotEmpty
		private List<UUID> categoryIds;
	}

	@Getter
	public static class UpdateCategory {
		@NotBlank
		private String name;
	}
}
