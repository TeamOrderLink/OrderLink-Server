package com.order.orderlink.user.presentation;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	 * @param request SignupRequest
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
}
