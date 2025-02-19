package com.order.orderlink.deliverydetail.presentation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
