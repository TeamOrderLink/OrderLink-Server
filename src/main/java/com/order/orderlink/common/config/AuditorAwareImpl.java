package com.order.orderlink.common.config;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {
		// 현재 로그인한 사용자의 ID 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		// UserDetails에서 UUID 가져오기 (예제에서는 String을 UUID로 변환)
		String userId = authentication.getName();
		return Optional.of(UUID.fromString(userId));
	}
}
