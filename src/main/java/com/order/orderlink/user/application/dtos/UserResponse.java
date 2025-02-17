package com.order.orderlink.user.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

	@Getter
	@AllArgsConstructor
	public static class Create {
		private UUID id;
	}

	@Getter
	@AllArgsConstructor
	public static class Read {
		private UUID id;
		private String username;
		private String email;
		private String phone;
		private String nickname;
		private String role;
		private Boolean isPublic;
		private LocalDateTime createdAt;
	}

	@Getter
	@AllArgsConstructor
	public static class Update {
		private UUID id;
		private String username;
		private String email;
		private String phone;
		private String nickname;
		private Boolean isPublic;
		private LocalDateTime updatedAt;
	}

	@Getter
	@AllArgsConstructor
	public static class Delete {
		private UUID id;
		private String username;
	}

}
