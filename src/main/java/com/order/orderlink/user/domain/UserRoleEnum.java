package com.order.orderlink.user.domain;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

	USER(Authority.USER),
	OWNER(Authority.OWNER),
	MANAGER(Authority.MANAGER),
	MASTER(Authority.MASTER);

	private final String authority;

	UserRoleEnum(String authority) {
		this.authority = authority;
	}

	public static class Authority {
		public static final String USER = "ROLE_USER";
		public static final String OWNER = "ROLE_ONWER";
		public static final String MANAGER = "ROLE_MANAGER";
		public static final String MASTER = "ROLE_MASTER";
	}

}
