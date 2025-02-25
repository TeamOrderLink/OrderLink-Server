package com.order.orderlink.common.auth.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.orderlink.common.auth.util.JwtUtil;
import com.order.orderlink.common.dtos.AuthRequest;
import com.order.orderlink.common.dtos.AuthResponse;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/api/auth/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			AuthRequest.Login requestDto = new ObjectMapper().readValue(request.getInputStream(),
				AuthRequest.Login.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getUsername(),
					requestDto.getPassword(),
					Collections.emptyList()
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException("로그인 요청 정보를 읽어오는데 실패했습니다.", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException {

		// JWT 토큰 생성
		String token = jwtUtil.createToken(authResult);

		// 응답 본문에 토큰을 담아서 반환
		AuthResponse.Login authResponse = new AuthResponse.Login(token);
		SuccessResponse<AuthResponse.Login> successResponse = SuccessResponse.success(SuccessCode.LOGIN_SUCCESS,
			authResponse);

		response.addHeader("Authorization", token);

		// JSON 형태로 직렬화해서 응답 본문에 추가
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String responseBody = new ObjectMapper().writeValueAsString(successResponse);
		response.getWriter().write(responseBody);
		response.getWriter().flush();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) {
		response.setStatus(401);
	}

}
