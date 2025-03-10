package com.order.orderlink.address.presentation;

import java.util.Arrays;
import java.util.List;
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

import com.order.orderlink.address.application.AddressService;
import com.order.orderlink.address.application.dtos.AddressRequest;
import com.order.orderlink.address.application.dtos.AddressResponse;
import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessNonDataResponse;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.user.domain.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

	private final AddressService addressService;

	/**
	 * 배송지 등록
	 * @param userDetails 인증된 사용자 정보
	 * @param request AddressRequest.Create
	 * @return SuccessResponse<AddressResponse.Create>
	 * @see AddressRequest.Create
	 * @see AddressResponse.Create
	 * @author Jihwan
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<AddressResponse.Create> createAddress(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody AddressRequest.Create request) {
		User user = userDetails.getUser();
		return SuccessResponse.success(SuccessCode.ADDRESS_CREATE_SUCCESS,
			addressService.createAddress(user, request));
	}

	/**
	 * 배송지 목록 조회
	 * @param userDetails 인증된 사용자 정보
	 * @param page 1-based 페이지 번호
	 * @param size 페이지 크기
	 * @param sort 정렬 기준 (예: createdAt,asc or updatedAt,desc)
	 * @return SuccessResponse<AddressResponse.ReadAll>
	 * @see AddressResponse.ReadAll
	 * @author Jihwan
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<AddressResponse.ReadAll> getAddresses(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String sort) {

		final int validatedPage = validatePage(page);
		final int validatedSize = validateSize(size);
		final Sort sortObj = createSortObject(sort);

		Pageable pageable = PageRequest.of(validatedPage - 1, validatedSize, sortObj);
		UUID userId = userDetails.getUser().getId();

		return SuccessResponse.success(
			SuccessCode.ADDRESS_GET_SUCCESS,
			addressService.getAddresses(userId, pageable)
		);
	}

	/**
	 * 배송지 상세 조회
	 * @param id 배송지 ID
	 * @return SuccessResponse<AddressResponse.Read>
	 * @see AddressResponse.Read
	 * @author Jihwan
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<AddressResponse.Read> getAddressInfo(@PathVariable("id") UUID id,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UUID currentUserId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.ADDRESS_GET_DETAIL_SUCCESS,
			addressService.getAddressInfo(id, currentUserId));
	}

	/**
	 * 배송지 수정
	 * @param id 배송지 ID
	 * @param request AddressRequest.Update
	 * @return SuccessResponse<AddressResponse.Update>
	 * @see AddressRequest.Update
	 * @see AddressResponse.Update
	 * @author Jihwan
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessResponse<AddressResponse.Update> updateAddress(@PathVariable("id") UUID id,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody AddressRequest.Update request) {
		UUID currentUserId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.ADDRESS_UPDATE_SUCCESS,
			addressService.updateAddress(id, currentUserId, request));
	}

	/**
	 * 배송지 삭제
	 * @param id 배송지 ID
	 * @param userDetails 인증된 사용자 정보
	 * @return SuccessNonDataResponse
	 * @see SuccessNonDataResponse
	 * @author Jihwan
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	public SuccessNonDataResponse deleteAddress(@PathVariable("id") UUID id,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UUID currentUserId = userDetails.getUser().getId();
		String username = userDetails.getUser().getUsername();
		addressService.deleteAddress(id, currentUserId, username);
		return SuccessNonDataResponse.success(SuccessCode.ADDRESS_DELETE_SUCCESS);
	}

	// 페이지 유효성 검증
	private int validatePage(int page) {
		return Math.max(page, 1);
	}

	// 페이지 크기 유효성 검증
	private int validateSize(int size) {
		final int DEFAULT_SIZE = 10;
		final List<Integer> ALLOWED_SIZES = Arrays.asList(10, 30, 50);
		return ALLOWED_SIZES.contains(size) ? size : DEFAULT_SIZE;
	}

	// 정렬 객체 생성
	private Sort createSortObject(String sortParam) {
		final Sort DEFAULT_SORT = Sort.by(
			Sort.Order.desc("createdAt"),
			Sort.Order.desc("updatedAt")
		);

		if (sortParam == null || sortParam.trim().isEmpty()) {
			return DEFAULT_SORT;
		}

		String[] sortParts = sortParam.split(",");
		if (sortParts.length == 2) {
			String field = sortParts[0].trim();
			String order = sortParts[1].trim().toLowerCase();

			if (List.of("id", "createdAt", "updatedAt").contains(field)) {
				if (order.equals("asc")) {
					return Sort.by(field).ascending();
				} else if (order.equals("desc")) {
					return Sort.by(field).descending();
				}
			}
		}
		return DEFAULT_SORT;
	}

}
