package com.order.orderlink.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {

	USER("ROLE_USER"),
	OWNER("ROLE_OWNER"),
	MANAGER("ROLE_MANAGER"),
	MASTER("ROLE_MASTER");

	private final String authority;

}
