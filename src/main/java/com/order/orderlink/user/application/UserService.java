package com.order.orderlink.user.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new RuntimeException("이미 사용 중인 아이디 입니다.");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("이미 사용 중인 email 입니다.");
		}
		if (userRepository.existsByNickname(request.getNickname())) {
			throw new RuntimeException("이미 사용 중인 닉네임 입니다.");
		}
		if (userRepository.existsByPhone(request.getPhone())) {
			throw new RuntimeException("이미 사용 중인 전화번호 입니다.");
		}

		// 비밀번호 암호화 및 유저 생성
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		User user = new User(request.getUsername(), request.getNickname(), request.getEmail(), request.getPhone(),
			encodedPassword, request.getRole());

		userRepository.save(user);

		return new UserResponse.Create(user.getId());
	}

}
