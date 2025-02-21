package com.order.orderlink.review.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Create {
		private UUID id;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Read {
		private UUID id;
		private UUID restaurantId;
		private UUID userId;
		private Integer rating;
		private String content;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}

}
