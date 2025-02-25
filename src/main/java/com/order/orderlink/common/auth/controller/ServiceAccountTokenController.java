package com.order.orderlink.common.auth.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "service-account-token-controller")
@RestController
@RequestMapping("/api/oauth")
public class ServiceAccountTokenController {

	@Value("${service.account.client-id}")
	private String validClientId;

	@Value("${service.account.client-secret}")
	private String validClientSecret;

	@Value("${spring.application.name}")
	private String issuer;

	@Value("${jwt.secret.key}")
	private String jwtSecretKey;

	// client_credentials 방식 토큰 발급
	@PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getToken(@RequestParam MultiValueMap<String, String> formData) {
		String grantType = formData.getFirst("grant_type");
		String clientId = formData.getFirst("client_id");
		String clientSecret = formData.getFirst("client_secret");

		Map<String, Object> response = new HashMap<>();

		// grantType 이 client_credential 인지 검증
		if (!"client_credentials".equals(grantType)) {
			response.put("error", "unsupported_grant_type");
			response.put("error_description", "Only client_credentials grant type is supported.");
			return response;
		}

		// clientId 와 clientSecret 검증
		if (!validClientId.equals(clientId) || !validClientSecret.equals(clientSecret)) {
			response.put("error", "invalid_client");
			response.put("error_description", "Client authentication failed.");
			return response;
		}

		// JWT 토큰 생성
		Date now = new Date();
		Date expiry = new Date(now.getTime() + 3600 * 1000L); // 1시간 유효

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecretKey));

		String jwtToken = "Bearer " + Jwts.builder()
			.header().add("typ", "JWT").and()
			.subject("serviceAccount")
			.claim("auth", "ROLE_SERVICE_ACCOUNT")
			.claim("userId", "serviceAccount")
			.issuer(issuer)
			.issuedAt(now)
			.expiration(expiry)
			.signWith(key, Jwts.SIG.HS512)
			.compact();

		response.put("access_token", jwtToken);
		response.put("token_type", "Bearer");
		response.put("expires_in", 3600);
		response.put("scope", "read write");
		response.put("issued_at", now.toString());
		return response;
	}

}
