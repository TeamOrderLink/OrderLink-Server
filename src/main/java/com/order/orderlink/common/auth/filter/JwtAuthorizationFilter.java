package com.order.orderlink.common.auth.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.orderlink.common.auth.UserDetailsServiceImpl;
import com.order.orderlink.common.auth.util.JwtUtil;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.common.exception.GlobalExceptionHandler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JWT 검증 및 인가")
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String tokenValue = jwtUtil.getJwtFromHeader(request);
		try {
			if (StringUtils.hasText(tokenValue)) {
				if (!jwtUtil.validateToken(tokenValue)) {
					log.error("토큰이 유효하지 않습니다.");
					throw new AuthException(ErrorCode.TOKEN_INVALID);
				}
				Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
				setAuthentication(info.getSubject());
			}
			filterChain.doFilter(request, response);
		} catch (JwtException ex) {
			log.debug(ex.getMessage());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			String jsonResponse = new ObjectMapper()
				.writeValueAsString(new GlobalExceptionHandler.ErrorResponse(
					HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
			response.getWriter().write(jsonResponse);
			response.getWriter().flush();
		}
	}

	// 인증 처리
	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(username);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
