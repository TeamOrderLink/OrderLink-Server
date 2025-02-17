package com.order.orderlink.user.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.UserException;
import com.order.orderlink.user.application.dtos.UserRequest;
import com.order.orderlink.user.application.dtos.UserResponse;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
			encodedPassword, request.getRole());

		userRepository.save(user);

		return new UserResponse.Create(user.getId());
	}

}
