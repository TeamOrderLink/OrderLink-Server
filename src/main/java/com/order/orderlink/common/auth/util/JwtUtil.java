package com.order.orderlink.common.auth.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.order.orderlink.common.auth.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

	@Value("${spring.application.name}")
	private String issuer;

	private final SecretKey secretKey;

	private final long EXPIRATION_TIME = 60 * 60 * 1000L; // 60분

	public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
	}

	// JWT 토큰 생성
	public String createToken(Authentication authentication) {
		Date now = new Date();
		Date accessExpiration = new Date(now.getTime() + this.EXPIRATION_TIME);

		return "Bearer " + Jwts.builder()
			.header().add("typ", "JWT").and()
			.subject(((UserDetailsImpl)authentication.getPrincipal()).getUsername())
			.claim("auth", ((UserDetailsImpl)authentication.getPrincipal()).getUser().getRole()) // TODO: role 추가
			.claim("userId", ((UserDetailsImpl)authentication.getPrincipal()).getUser().getId())
			.issuer(issuer)
			.issuedAt(now)
			.expiration(accessExpiration)
			.signWith(secretKey, Jwts.SIG.HS512)
			.compact();
	}

	// header 에서 JWT 가져오기
	public String getJwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	// JWT 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			if (e instanceof SecurityException) {
				log.debug("[SecurityException] 잘못된 토큰");
				throw new JwtException("[SecurityException]: 잘못된 토큰입니다.");
			} else if (e instanceof MalformedJwtException) {
				log.debug("[MalformedJwtException] 잘못된 토큰");
				throw new JwtException("[MalformedJwtException]: 잘못된 토큰입니다.");
			} else if (e instanceof ExpiredJwtException) {
				log.debug("[ExpiredJwtException] 토큰 만료");
				throw new JwtException("[ExpiredJwtException]: 토큰이 만료되었습니다.");
			} else if (e instanceof UnsupportedJwtException) {
				log.debug("[UnsupportedJwtException] 지원되지 않는 토큰");
				throw new JwtException("[UnsupportedJwtException]: 지원되지 않는 토큰입니다.");
			} else if (e instanceof IllegalArgumentException) {
				log.debug("[IllegalArgumentException]");
				throw new JwtException("[IllegalArgumentException]: 토큰이 비어있습니다.");
			} else {
				log.debug("[토큰검증 오류] {}", e.getClass());
				throw new JwtException("[토큰검증 오류]: 미처리 토큰 오류");
			}
		}
	}

	// JWT 토큰에서 사용자 정보(Claims) 추출
	public Claims getUserInfoFromToken(String jwt) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(jwt)
			.getPayload();
	}

}
