package com.order.orderlink.user.application.dtos;

import com.order.orderlink.user.domain.UserRoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserRequest {

	@Getter
	public static class Create {

		@Size(min = 4, max = 10, message = "아이디는 4자 이상 10자 이하로 입력해주세요.")
		@Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 소문자와 숫자만 가능합니다.")
		@NotBlank(message = "아이디는 필수 항목입니다.")
		private String username;

		@Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하이어야 합니다.")
		@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,15}$",
			message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
		@NotBlank(message = "비밀번호는 필수 항목입니다.")
		private String password;

		@Email(message = "이메일 형식에 맞게 입력해주세요.")
		@NotBlank(message = "이메일은 필수 항목입니다.")
		private String email;

		@Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 '-' 없이 10자리 또는 11자리 숫자로 입력해주세요.")
		@NotBlank(message = "전화번호는 필수 항목입니다.")
		private String phone;

		@NotBlank(message = "닉네임은 필수 항목입니다.")
		private String nickname;

		@NotNull(message = "Role은 필수 항목입니다.")
		private UserRoleEnum role;
	}

	@Getter
	public static class Read {

	}

	@Getter
	public static class Update {

		private String nickname;

		@Email(message = "이메일 형식에 맞게 입력해주세요.")
		private String email;

		@Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 '-' 없이 10자리 또는 11자리 숫자로 입력해주세요.")
		private String phone;

		private Boolean isPublic;
	}

	@Getter
	public static class ChangePassword {

		@NotNull(message = "현재 비밀번호는 필수 항목입니다.")
		private String currentPassword;

		@Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하이어야 합니다.")
		@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,15}$",
			message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
		@NotNull(message = "새 비밀번호는 필수 항목입니다.")
		private String newPassword;
	}

	@Getter
	public static class Delete {

	}

}
