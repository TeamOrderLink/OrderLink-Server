package com.order.orderlink.user.application;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.UserException;
import com.order.orderlink.user.application.dtos.UserRequest;
import com.order.orderlink.user.application.dtos.UserResponse;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.UserRoleEnum;
import com.order.orderlink.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// 회원가입
	public UserResponse.Create signup(UserRequest.Create request) {
		// 중복 체크
		if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())
			|| userRepository.existsByNickname(request.getNickname()) || userRepository.existsByPhone(
			request.getPhone())) {
			throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
		}
		// 비밀번호 암호화 및 유저 생성
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		User user = new User(request.getUsername(), request.getNickname(), request.getEmail(), request.getPhone(),
			encodedPassword, UserRoleEnum.valueOf(request.getRole()));

		userRepository.save(user);

		return new UserResponse.Create(user.getId());
	}

	// 내 정보 조회
	@Transactional(readOnly = true)
	public UserResponse.Read getMyInfo(UUID userId) {
		User user = getUser(userId);
		return new UserResponse.Read(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(),
			user.getNickname(), user.getRole().name(), user.getIsPublic(), user.getCreatedAt());
	}

	// 사용자 정보 수정
	public UserResponse.Update updateInfo(UUID userId, UserRequest.Update request) {
		User user = getUser(userId);

		user.updateInfo(request.getEmail(), request.getPhone(), request.getNickname(), request.getIsPublic());
		userRepository.save(user);

		return new UserResponse.Update(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(),
			user.getNickname(), user.getIsPublic(), user.getUpdatedAt());
	}

	// 비밀번호 변경
	public UserResponse.UpdatePassword updatePassword(UUID userId, UserRequest.UpdatePassword request) {
		User user = getUser(userId);

		// 현재 비밀번호가 일치하는지 확인
		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new UserException(ErrorCode.USER_PASSWORD_NOT_MATCH);
		}

		// 새 비밀번호와 확인용 비밀번호가 일치하는지 확인
		if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
			throw new UserException(ErrorCode.USER_CONFIRM_PASSWORD_NOT_MATCH);
		}

		// 새 비밀번호 암호화 및 변경
		user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);

		return new UserResponse.UpdatePassword(user.getId(), user.getUpdatedAt());
	}

	// 사용자 탈퇴
	public UserResponse.Delete softDeleteUser(UUID userId, String username, String password) {
		User user = getUser(userId);

		// 입력한 비밀번호가 일치하는지 확인
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UserException(ErrorCode.USER_PASSWORD_NOT_MATCH);
		}

		user.softDelete(username);  // BaseTimeEntity에 구현한 softDelete 메서드 (deletedBy, deletedAt 필드 업데이트)

		return new UserResponse.Delete(user.getId(), user.getDeletedAt());
	}

	private User getUser(UUID userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
	}
}
