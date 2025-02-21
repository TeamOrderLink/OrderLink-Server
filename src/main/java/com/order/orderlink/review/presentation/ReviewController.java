package com.order.orderlink.review.presentation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessNonDataResponse;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.common.exception.OrderException;
import com.order.orderlink.common.exception.RestaurantException;
import com.order.orderlink.common.exception.ReviewException;
import com.order.orderlink.review.application.ReviewService;
import com.order.orderlink.review.application.dtos.ReviewRequest;
import com.order.orderlink.review.application.dtos.ReviewResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 등록
	 * @param orderIdStr 주문 ID
	 * @param userDetails 로그인한 사용자 정보
	 * @param request 리뷰 생성 요청 정보 (평점, 내용)
	 * @return 생성된 리뷰 ID
	 * @throws OrderException 주문이 존재하지 않는 경우
	 * @throws ReviewException 리뷰 작성이 허용되지 않는 경우, 이미 리뷰가 작성된 경우
	 * @throws RestaurantException 음식점이 존재하지 않는 경우
	 * @see ReviewService#createReview(UUID, UUID, ReviewRequest.Create)
	 * @author Jihwan
	 */
	@PostMapping("/orders/{orderId}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<ReviewResponse.Create> createReview(@PathVariable("orderId") String orderIdStr,
		@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ReviewRequest.Create request) {

		UUID orderId = UUID.fromString(orderIdStr);
		UUID userId = userDetails.getUser().getId();

		return SuccessResponse.success(SuccessCode.REVIEW_CREATE_SUCCESS,
			reviewService.createReview(orderId, userId, request));
	}

	/**
	 * 리뷰 수정
	 * @param reviewId 리뷰 ID
	 * @param userDetails 로그인한 사용자 정보
	 * @param request 리뷰 수정 요청 정보 (평점, 내용)
	 * @return 수정된 리뷰 정보 (리뷰 ID, 음식점 ID, 사용자 ID, 평점, 내용, 생성일, 수정일)
	 * @throws ReviewException 리뷰 수정이 허용되지 않는 경우
	 * @author Jihwan
	 */
	@PutMapping("/{reviewId}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<ReviewResponse.Read> updateReview(@PathVariable("reviewId") UUID reviewId,
		@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ReviewRequest.Update request) {
		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.REVIEW_UPDATE_SUCCESS,
			reviewService.updateReview(reviewId, userId, request));
	}

	/**
	 * 리뷰 삭제
	 * @param reviewId 리뷰 ID
	 * @param userDetails 로그인한 사용자 정보
	 * @return 성공 응답 (성공 코드)
	 * @throws ReviewException 리뷰 삭제가 허용되지 않는 경우
	 * @author Jihwan
	 */
	@DeleteMapping("/{reviewId}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessNonDataResponse deleteReview(@PathVariable("reviewId") UUID reviewId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UUID userId = userDetails.getUser().getId();
		String username = userDetails.getUser().getUsername();
		reviewService.deleteReview(reviewId, userId, username);
		return SuccessNonDataResponse.success(SuccessCode.REVIEW_DELETE_SUCCESS);
	}

}
