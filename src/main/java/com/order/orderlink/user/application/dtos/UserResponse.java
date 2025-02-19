package com.order.orderlink.user.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
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
	public static class ReadUserList {
		private List<Read> users;
		private int currentPage;
		private int totalPages;
		private long totalElements;
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
	}

	@Getter
	@AllArgsConstructor
	public static class UpdateByAdmin {
		private UUID id;
		private String username;
		private String email;
		private String phone;
		private String nickname;
		private Boolean isPublic;
		private String role;
	}

	@Getter
	@AllArgsConstructor
	public static class UpdatePassword {
		private UUID id;
	}

	@Getter
	@AllArgsConstructor
	public static class Delete {
		private UUID id;
		private LocalDateTime deletedAt;
	}

}
