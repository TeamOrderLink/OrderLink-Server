package com.order.orderlink.user.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.user.application.UserService;
import com.order.orderlink.user.application.dtos.UserRequest;
import com.order.orderlink.user.application.dtos.UserResponse;
import com.order.orderlink.user.domain.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 10;
	private static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(10, 30, 50);
	private static final Sort DEFAULT_SORT = Sort.by(
		Sort.Order.desc("createdAt"),
		Sort.Order.desc("updatedAt")
	);

	/**
	 * 회원 가입
	 * @param request UserRequest.Create
	 * @return SuccessResponse<UserResponse.Create>
	 * @see UserRequest.Create
	 * @see UserResponse.Create
	 * @author Jihwan
	 */
	@PostMapping
	public SuccessResponse<UserResponse.Create> signup(@Valid @RequestBody UserRequest.Create request) {
		return SuccessResponse.success(SuccessCode.USER_CREATE_SUCCESS, userService.signup(request));
	}

	/**
	 * 관리자 권한으로 전체 회원 조회
	 * @param page 1-based 페이지 번호
	 * @param size 페이지 크기
	 * @param sort 정렬 기준 (예: createdAt,asc or updatedAt,desc)
	 * @return SuccessResponse<UserResponse.ReadUserList>
	 */
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<UserResponse.ReadUserList> getAllUsers(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String sort) {

		page = validatePage(page);
		size = validatePageSize(size);

		Sort sortObj = parseSort(sort);
		Pageable pageable = PageRequest.of(page - 1, size, sortObj);

		return SuccessResponse.success(SuccessCode.USER_GET_SUCCESS, userService.getAllUsers(pageable));
	}

	/**
	 * 내 정보 조회
	 * @param userDetails 인증된 사용자 정보
	 * @return SuccessResponse<UserResponse.Read>
	 * @see UserResponse.Read
	 * @author Jihwan
	 */
	@GetMapping("/me")
	public SuccessResponse<UserResponse.Read> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.USER_GET_DETAIL_SUCCESS, userService.getMyInfo(userId));
	}

	/**
	 * 관리자 권한으로 특정 회원 정보 조회
	 * @param userId 조회할 사용자의 UUID
	 * @return SuccessResponse<UserResponse.Read>
	 * @see UserResponse.Read
	 * @author Jihwan
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<UserResponse.Read> getUserInfoByAdmin(@PathVariable("id") UUID userId) {
		return SuccessResponse.success(SuccessCode.USER_GET_DETAIL_SUCCESS, userService.getUserInfoByAdmin(userId));
	}

	/**
	 * 회원 정보 수정
	 * @param userDetails 인증된 사용자 정보
	 * @param request UserRequest.Update
	 * @return SuccessResponse<UserResponse.Update>
	 * @see UserRequest.Update
	 * @see UserResponse.Update
	 * @author Jihwan
	 */
	@PutMapping("/me")
	public SuccessResponse<UserResponse.Update> updateInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UserRequest.Update request) {
		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.USER_UPDATE_SUCCESS, userService.updateInfo(userId, request));
	}

	/**
	 * 관리자 권한으로 특정 회원 정보 수정
	 * @param userId 수정할 사용자의 UUID
	 * @param request UserRequest.UpdateByAdmin
	 * @return SuccessResponse<UserResponse.UpdateByAdmin>
	 * @see UserRequest.UpdateByAdmin
	 * @see UserResponse.UpdateByAdmin
	 * @author Jihwan
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<UserResponse.UpdateByAdmin> updateInfoByAdmin(@PathVariable("id") UUID userId,
		@Valid @RequestBody UserRequest.UpdateByAdmin request) {
		return SuccessResponse.success(SuccessCode.USER_UPDATE_SUCCESS, userService.updateInfoByAdmin(userId, request));
	}

	/**
	 * 비밀번호 변경
	 * @param userDetails 인증된 사용자 정보
	 * @param request UserRequest.UpdatePassword
	 * @return SuccessResponse<UserResponse.UpdatePassword>
	 * @see UserRequest.UpdatePassword
	 * @see UserResponse.UpdatePassword
	 * @author Jihwan
	 */
	@PutMapping("/me/password")
	public SuccessResponse<UserResponse.UpdatePassword> updatePassword(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UserRequest.UpdatePassword request) {
		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.USER_PASSWORD_UPDATE_SUCCESS,
			userService.updatePassword(userId, request));
	}

	/**
	 * 회원 삭제/탈퇴
	 * @param userDetails 인증된 사용자 정보
	 * @param request UserRequest.Delete
	 * @return SuccessResponse<UserResponse.Delete>
	 * @see UserRequest.Delete
	 * @see UserResponse.Delete
	 * @author Jihwan
	 */
	@DeleteMapping("/me")
	public SuccessResponse<UserResponse.Delete> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UserRequest.Delete request) {
		UUID userId = userDetails.getUser().getId();
		String username = userDetails.getUser().getUsername();
		return SuccessResponse.success(SuccessCode.USER_DELETE_SUCCESS,
			userService.softDeleteUser(userId, username, request.getPassword()));
	}

	@GetMapping("/{userId}/getUser")
	public ResponseEntity<User> getUser(@PathVariable UUID userId) {
		User user = userService.getUser(userId);
		return ResponseEntity.ok(user);
	}

	// 페이지 번호 유효성 검증
	private int validatePage(int page) {
		return Math.max(page, DEFAULT_PAGE);
	}

	// 페이지 크기 유효성 검증
	private int validatePageSize(int size) {
		return ALLOWED_PAGE_SIZES.contains(size) ? size : DEFAULT_SIZE;
	}

	// 정렬 파라미터 파싱
	private Sort parseSort(String sort) {
		if (sort == null || sort.trim().isEmpty()) {
			return DEFAULT_SORT;
		}

		String[] sortParts = sort.split(",");
		if (sortParts.length == 2) {
			String field = sortParts[0].trim();
			String order = sortParts[1].trim().toLowerCase();

			if (isValidSortField(field)) {
				return order.equals("asc")
					? Sort.by(field).ascending()
					: Sort.by(field).descending();
			}
		}
		return DEFAULT_SORT;
	}

	// 유효한 정렬 필드 확인
	private boolean isValidSortField(String field) {
		return Arrays.asList("id", "username", "email", "phone", "nickname", "createdAt", "updatedAt")
			.contains(field);
	}

}
