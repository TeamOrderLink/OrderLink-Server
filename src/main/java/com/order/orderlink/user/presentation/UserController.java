package com.order.orderlink.user.presentation;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.user.application.UserService;
import com.order.orderlink.user.application.dtos.UserRequest;
import com.order.orderlink.user.application.dtos.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 회원가입
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
	 * 내 정보 조회
	 * @param userDetails 인증된 사용자 정보
	 * @return SuccessResponse<UserResponse.Read>
	 * @see UserResponse.Read
	 * @author Jihwan
	 */
	@GetMapping("/me")
	public SuccessResponse<UserResponse.Read> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.USER_READ_SUCCESS, userService.getMyInfo(userId));
	}

	/**
	 * 사용자 정보 수정
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
	 * 사용자 탈퇴
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

}
