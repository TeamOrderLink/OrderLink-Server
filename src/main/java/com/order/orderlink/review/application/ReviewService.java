package com.order.orderlink.review.application;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.OrderException;
import com.order.orderlink.common.exception.RestaurantException;
import com.order.orderlink.common.exception.ReviewException;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.repository.OrderRepository;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;
import com.order.orderlink.review.application.dtos.ReviewRequest;
import com.order.orderlink.review.application.dtos.ReviewResponse;
import com.order.orderlink.review.domain.Review;
import com.order.orderlink.review.domain.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;
	private final RestaurantRepository restaurantRepository;

	/**
	 * 리뷰 등록:
	 * 주문자만 리뷰를 작성할 수 있으며, 한 주문당 한 개의 리뷰만 작성가능.
	 * 리뷰는 해당 주문을 한 음식점에 대해서 리뷰를 작성.
	 * 주문에 대한 리뷰가 이미 존재하면 예외 발생.
	 * 주문의 userId와 로그인한 사용자가 동일한 경우에만 작성 가능.
	 * @param orderId 주문 ID
	 * @param currentUserId 로그인한 사용자 ID
	 * @param request 리뷰 생성 요청
	 * @return 생성된 리뷰 ID
	 * @throws OrderException 주문이 존재하지 않는 경우
	 * @throws ReviewException 리뷰 작성이 허용되지 않는 경우, 이미 리뷰가 작성된 경우
	 * @throws RestaurantException 음식점이 존재하지 않는 경우
	 * @author Jihwan
	 */
	public ReviewResponse.Create createReview(UUID orderId, UUID currentUserId, ReviewRequest.Create request) {

		// 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));

		// 주문자와 현재 로그인한 사용자가 동일한지 확인
		if (!order.getUserId().equals(currentUserId)) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_ALLOWED);
		}

		// 이미 특정 주문에 리뷰가 작성되었는지 확인
		if (reviewRepository.existsByOrderId(orderId)) {
			throw new ReviewException(ErrorCode.REVIEW_ALREADY_EXISTS);
		}

		UUID restaurantId = order.getRestaurantId();
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new RestaurantException(ErrorCode.RESTAURANT_NOT_FOUND));

		// 리뷰 생성
		Review review = Review.builder()
			.restaurant(restaurant)
			.userId(currentUserId)
			.orderId(orderId)
			.rating(request.getRating())
			.content(request.getContent())
			.build();
		reviewRepository.save(review);

		// 음식점 평점 집계 업데이트
		Double newRatingSum = restaurant.getRatingSum() + review.getRating();
		Integer newRatingCount = restaurant.getRatingCount() + 1;
		restaurant.updateRatingSum(newRatingSum);
		restaurant.updateRatingCount(newRatingCount);
		restaurant.updateAvgRating(newRatingSum / newRatingCount);

		return new ReviewResponse.Create(review.getId());
	}

	// 특정 음식점에 대한 모든 리뷰 조회
	public ReviewResponse.ReadAsPage getReviewsByRestaurant(UUID restaurantId, Pageable pageable) {
		Page<Review> page = reviewRepository.findByRestaurantId(restaurantId, pageable);
		List<ReviewResponse.Read> reviews = page.getContent().stream().map(review ->
				ReviewResponse.Read.builder()
					.id(review.getId())
					.restaurantId(review.getRestaurant().getId())
					.userId(review.getUserId())
					.rating(review.getRating())
					.content(review.getContent())
					.createdAt(review.getCreatedAt())
					.updatedAt(review.getUpdatedAt())
					.build())
			.toList();
		return new ReviewResponse.ReadAsPage(reviews, page.getNumber() + 1, page.getTotalPages(),
			page.getTotalElements());
	}

	// 리뷰 수정
	public ReviewResponse.Read updateReview(UUID reviewId, UUID currentUserId, ReviewRequest.Update request) {
		Review review = getReview(reviewId);

		// 리뷰 작성자와 현재 로그인한 사용자가 동일한지 확인
		if (!review.getUserId().equals(currentUserId)) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_ALLOWED);
		}

		Restaurant restaurant = review.getRestaurant();
		Integer oldRating = review.getRating();
		Integer newRating = request.getRating();

		review.update(newRating, request.getContent());
		reviewRepository.save(review);

		// 음식점 평점 집계 업데이트
		if (newRating != null && !newRating.equals(oldRating)) {
			Integer diff = review.getRating() - oldRating;
			Double newRatingSum = restaurant.getRatingSum() + diff;
			restaurant.updateRatingSum(newRatingSum);
			restaurant.updateAvgRating(newRatingSum / restaurant.getRatingCount());
		}

		return ReviewResponse.Read.builder()
			.id(review.getId())
			.restaurantId(restaurant.getId())
			.userId(review.getUserId())
			.rating(review.getRating())
			.content(review.getContent())
			.createdAt(review.getCreatedAt())
			.updatedAt(review.getUpdatedAt())
			.build();
	}

	// 리뷰 삭제
	public void deleteReview(UUID reviewId, UUID currentUserId, String currentUsername) {
		Review review = getReview(reviewId);

		// 리뷰 작성자와 현재 로그인한 사용자가 동일한지 확인
		if (!review.getUserId().equals(currentUserId)) {
			throw new ReviewException(ErrorCode.REVIEW_NOT_ALLOWED);
		}

		Restaurant restaurant = review.getRestaurant();
		review.softDelete(currentUsername);

		// 음식점 평점 집계 업데이트
		Double newRatingSum = restaurant.getRatingSum() - review.getRating();
		Integer newRatingCount = restaurant.getRatingCount() - 1;
		restaurant.updateRatingSum(newRatingSum);
		restaurant.updateRatingCount(newRatingCount);
		if (newRatingCount > 0) {
			restaurant.updateAvgRating(newRatingSum / newRatingCount);
		} else {
			restaurant.updateAvgRating(0.0);
		}
	}

	private Review getReview(UUID reviewId) {
		return reviewRepository.findById(reviewId)
			.orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
	}

}
