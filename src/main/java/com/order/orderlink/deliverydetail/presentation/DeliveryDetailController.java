package com.order.orderlink.deliverydetail.presentation;

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
import com.order.orderlink.deliverydetail.application.dtos.DeliveryDetailRequest;
import com.order.orderlink.deliverydetail.application.dtos.DeliveryDetailResponse;
import com.order.orderlink.deliverydetail.application.dtos.DeliveryDetailService;
import com.order.orderlink.user.domain.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryDetailController {

	private final DeliveryDetailService deliveryDetailService;

	/**
	 * 배송 상세 생성
	 * @param userDetails 인증된 사용자 정보
	 * @param request DeliveryDetailRequest.Create
	 * @return SuccessResponse<DeliveryDetailResponse.Create>
	 * @see DeliveryDetailRequest.Create
	 * @see DeliveryDetailResponse.Create
	 * @author Jihwan
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<DeliveryDetailResponse.Create> createDeliveryDetail(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody DeliveryDetailRequest.Create request) {
		User user = userDetails.getUser();
		return SuccessResponse.success(SuccessCode.DELIVERY_DETAIL_CREATE_SUCCESS,
			deliveryDetailService.createDeliveryDetail(user, request));
	}

	/**
	 * 배송 상세 전체 목록 조회
	 * @param userDetails 인증된 사용자 정보
	 * @param page 1-based 페이지 번호
	 * @param size 페이지 크기
	 * @param sort 정렬 기준 (예: createdAt,asc or updatedAt,desc)
	 * @return SuccessResponse<DeliveryDetailResponse.ReadAll>
	 * @see DeliveryDetailResponse.ReadAll
	 * @author Jihwan
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<DeliveryDetailResponse.ReadAll> getDeliveryDetails(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String sort) {

		if (page < 1) {
			page = 1;
		}

		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}

		Sort sortObj = null;
		if (sort != null && !sort.trim().isEmpty()) {
			String[] sortParts = sort.split(",");

			if (sortParts.length == 2) {
				String fieldInput = sortParts[0].trim();
				String orderInput = sortParts[1].trim().toLowerCase();

				if (!fieldInput.isEmpty() && (orderInput.equals("asc") || orderInput.equals("desc"))) {
					sortObj = orderInput.equals("asc") ?
						Sort.by(fieldInput).ascending() : Sort.by(fieldInput).descending();
				}
			}
		}

		if (sortObj == null) {
			sortObj = Sort.by(
				Sort.Order.desc("createdAt"),
				Sort.Order.desc("updatedAt")
			);
		}

		Pageable pageable = PageRequest.of(page - 1, size, sortObj);
		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.DELIVERY_DETAIL_READ_ALL_SUCCESS,
			deliveryDetailService.getDeliveryDetails(userId, pageable));
	}

	/**
	 * 배송 상세 단건 조회
	 * @param deliveryDetailId 배송 상세 ID
	 * @return SuccessResponse<DeliveryDetailResponse.Read>
	 * @see DeliveryDetailResponse.Read
	 * @author Jihwan
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<DeliveryDetailResponse.Read> getDeliveryDetail(@PathVariable("id") UUID deliveryDetailId) {
		return SuccessResponse.success(SuccessCode.DELIVERY_DETAIL_READ_SUCCESS,
			deliveryDetailService.getDeliveryDetail(deliveryDetailId));
	}

	/**
	 * 배송 상세 수정
	 * @param deliveryDetailId 배송 상세 ID
	 * @param request DeliveryDetailRequest.Update
	 * @return SuccessResponse<DeliveryDetailResponse.Update>
	 * @see DeliveryDetailRequest.Update
	 * @see DeliveryDetailResponse.Update
	 * @author Jihwan
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<DeliveryDetailResponse.Update> updateDeliveryDetail(
		@PathVariable("id") UUID deliveryDetailId,
		@Valid @RequestBody DeliveryDetailRequest.Update request) {
		return SuccessResponse.success(SuccessCode.DELIVERY_DETAIL_UPDATE_SUCCESS,
			deliveryDetailService.updateDeliveryDetail(deliveryDetailId, request));
	}

	/**
	 * 배송 상세 삭제
	 * @param deliveryDetailId 배송 상세 ID
	 * @param userDetails 인증된 사용자 정보
	 * @return SuccessNonDataResponse
	 * @see SuccessNonDataResponse
	 * @author Jihwan
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessNonDataResponse deleteDeliveryDetail(@PathVariable("id") UUID deliveryDetailId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String username = userDetails.getUser().getUsername();
		deliveryDetailService.deleteDeliveryDetail(deliveryDetailId, username);
		return SuccessNonDataResponse.success(SuccessCode.DELIVERY_DETAIL_DELETE_SUCCESS);
	}

}
