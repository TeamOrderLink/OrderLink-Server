package com.order.orderlink.review.presentation;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessNonDataResponse;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.order.exception.OrderException;
import com.order.orderlink.restaurant.exception.RestaurantException;
import com.order.orderlink.review.application.ReviewService;
import com.order.orderlink.review.application.dtos.ReviewRequest;
import com.order.orderlink.review.application.dtos.ReviewResponse;
import com.order.orderlink.review.exception.ReviewException;

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
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<ReviewResponse.Create> createReview(@RequestParam("orderId") String orderIdStr,
		@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ReviewRequest.Create request) {

		UUID orderId = UUID.fromString(orderIdStr);
		UUID userId = userDetails.getUser().getId();

		return SuccessResponse.success(SuccessCode.REVIEW_CREATE_SUCCESS,
			reviewService.createReview(orderId, userId, request));
	}

	/**
	 * 특정 음식점에 대한 페이징 처리된 리뷰 목록 조회
	 * @param restaurantId 음식점 ID
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @param sort 정렬 조건 (필드명, 정렬 방향)
	 * @return 페이징 처리된 리뷰 목록 (리뷰 ID, 음식점 ID, 사용자 ID, 평점, 내용, 생성일, 수정일)
	 * @see ReviewResponse.ReviewPageResponse
	 * @author Jihwan
	 */
	@GetMapping
	public SuccessResponse<ReviewResponse.ReviewPageResponse> getReviewsByRestaurant(
		@RequestParam("restaurantId") UUID restaurantId, @RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sort) {

		// 페이지 번호가 1보다 작으면 1로 설정
		if (page < 1) {
			page = 1;
		}

		// 허용된 사이즈: 10, 30, 50 (그 외면 기본 10)
		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}

		Sort sortObj = null;
		if (sort != null && !sort.trim().isEmpty()) {
			String[] sortParts = sort.split(",");
			if (sortParts.length == 2) {
				String fieldInput = sortParts[0].trim();
				String orderInput = sortParts[1].trim().toLowerCase();
				// field가 "rating", "createdAt", "updatedAt" 중 하나이면 사용,
				// 아니면 기본 정렬 적용
				if (fieldInput.equals("rating") || fieldInput.equals("createdAt") || fieldInput.equals("updatedAt")) {
					if (orderInput.equals("asc")) {
						sortObj = Sort.by(fieldInput).ascending();
					} else if (orderInput.equals("desc")) {
						sortObj = Sort.by(fieldInput).descending();
					}
				} else {
					// 유효하지 않은 field이면 기본값 적용
					sortObj = Sort.by(
						Sort.Order.desc("createdAt"),
						Sort.Order.desc("updatedAt")
					);
				}
			} else {
				sortObj = Sort.by(
					Sort.Order.desc("createdAt"),
					Sort.Order.desc("updatedAt")
				);
			}
		} else {
			sortObj = Sort.by(
				Sort.Order.desc("createdAt"),
				Sort.Order.desc("updatedAt")
			);
		}

		// 1-based 페이지 번호를 0-based로 변환
		assert sortObj != null;
		Pageable pageable = PageRequest.of(page - 1, size, sortObj);
		return SuccessResponse.success(SuccessCode.REVIEW_GET_SUCCESS,
			reviewService.getReviewsByRestaurant(restaurantId, pageable));
	}

	/**
	 * 리뷰 상세 조회
	 * @param reviewId 리뷰 ID
	 * @return 리뷰 상세 정보 (리뷰 ID, 사용자 닉네임, 평점, 내용, 생성일, 수정일)
	 * @see ReviewResponse.Detail
	 * @author Jihwan
	 */
	@GetMapping("/{reviewId}")
	public SuccessResponse<ReviewResponse.Detail> getReviewDetail(@PathVariable("reviewId") UUID reviewId) {
		return SuccessResponse.success(SuccessCode.REVIEW_GET_DETAIL_SUCCESS, reviewService.getReviewDetail(reviewId));
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
	public SuccessResponse<ReviewResponse.Update> updateReview(@PathVariable("reviewId") UUID reviewId,
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
