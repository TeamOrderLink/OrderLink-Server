package com.order.orderlink.common.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.order.orderlink.common.auth.UserDetailsImpl;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		// UserDetails에서 이름 가져오기
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		return Optional.of(userDetails.getUsername());
	}
}
