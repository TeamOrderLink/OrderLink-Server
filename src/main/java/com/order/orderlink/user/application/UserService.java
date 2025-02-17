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
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// 회원가입
	@Transactional
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
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
		return new UserResponse.Read(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(),
			user.getNickname(), user.getRole().name(), user.getIsPublic(), user.getCreatedAt());
	}

	// 사용자 정보 수정
	@Transactional
	public UserResponse.Update updateInfo(UUID userId, UserRequest.Update request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		user.updateInfo(request.getEmail(), request.getPhone(), request.getNickname(), request.getIsPublic());
		userRepository.save(user);

		return new UserResponse.Update(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(),
			user.getNickname(), user.getIsPublic(), user.getUpdatedAt());
	}

	// 사용자 탈퇴
	@Transactional
	public UserResponse.Delete softDeleteUser(UUID userId, String username, String password) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		// 입력한 비밀번호가 일치하는지 확인
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UserException(ErrorCode.USER_PASSWORD_NOT_MATCH);
		}

		user.softDelete(username);  // BaseTimeEntity에 구현한 softDelete 메서드
		userRepository.save(user);

		return new UserResponse.Delete(user.getId(), user.getUsername());
	}
}
