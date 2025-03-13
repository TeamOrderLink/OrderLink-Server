package com.order.orderlink.review.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {
		private UUID reviewId;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Update {
		private UUID reviewId;
		private UUID restaurantId;
		private UUID userId;
		private Integer rating;
		private String content;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ReviewPageResponse {
		private List<ReviewResponse.Summary> reviews;
		private Integer currentPage; // 1-based
		private Integer totalPages;
		private Long totalElements;
	}

	// 리뷰 목록 조회 응답 DTO (간략한 정보)
	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Summary {
		private UUID reviewId;
		private String userNickname; // 리뷰 작성자의 닉네임
		private Integer rating;
		private String contentSummary; // 리뷰 내용의 일부 (50자 이내 요약)
		private LocalDateTime createdAt;
	}

	// 리뷰 상세 조회 응답 DTO (자세한 정보)
	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Detail {
		private UUID reviewId;
		private String userNickname;
		private Integer rating;
		private String fullContentText; // 리뷰 내용 전체
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private OrderDetails orderDetails;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class OrderDetails {
		private UUID orderId;
		private LocalDateTime orderDate;
		private List<String> orderItems;
	}

}
