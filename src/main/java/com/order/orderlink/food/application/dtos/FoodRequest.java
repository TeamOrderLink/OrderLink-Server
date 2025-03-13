package com.order.orderlink.food.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FoodRequest {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {

		@NotBlank(message = "음식명을 입력해주세요.")
		private String name;

		private String description;

		@NotNull(message = "가격을 입력해주세요.")
		@Positive(message = "가격은 0원 이상이여야 합니다.")
		private int price;

		private boolean isHidden;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Update {

		private String name;

		private String description;

		@Positive(message = "가격은 0원 이상이여야 합니다.")
		private int price;

		private boolean isHidden;

	}
}
