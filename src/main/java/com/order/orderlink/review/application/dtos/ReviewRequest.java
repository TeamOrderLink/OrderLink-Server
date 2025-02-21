package com.order.orderlink.review.application.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class ReviewRequest {

	@Getter
	@Setter
	public static class Create {

		@NotNull(message = "평점은 필수입니다.")
		@Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
		@Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
		private Integer rating;

		@Size(max = 500, message = "리뷰는 최대 500자까지 입력 가능합니다.")
		private String content;
	}

	@Getter
	@Setter
	public static class Update {

		@Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
		@Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
		private Integer rating;

		@Size(max = 500, message = "리뷰는 최대 500자까지 입력 가능합니다.")
		private String content;

	}

}
