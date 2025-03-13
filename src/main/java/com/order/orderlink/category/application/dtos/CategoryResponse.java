package com.order.orderlink.category.application.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CategoryResponse {
	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {
		private final UUID categoryId;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class GetCateories {
		private final List<CategoryDTO> categories;
	}
}
