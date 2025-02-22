package com.order.orderlink.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.orderlink.address.domain.Address;
import com.order.orderlink.common.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@Table(name = "p_users")
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "nickname", unique = true, nullable = false)
	private String nickname;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "phone", unique = true, nullable = false)
	private String phone;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "is_public", nullable = false, columnDefinition = "boolean default true")
	private Boolean isPublic;

	@Column(name = "role", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Address> addresses = new ArrayList<>();

	@Builder
	public User(String username, String nickname, String email, String phone, String password, UserRoleEnum role) {
		this.username = username;
		this.nickname = nickname;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.isPublic = true;
	}

	public void updateInfo(String email, String phone, String nickname, Boolean isPublic) {

		if (email != null && !email.trim().isEmpty()) {
			this.email = email;
		}
		if (phone != null && !phone.trim().isEmpty()) {
			this.phone = phone;
		}
		if (nickname != null && !nickname.trim().isEmpty()) {
			this.nickname = nickname;
		}
		if (isPublic != null) {
			this.isPublic = isPublic;
		}
	}

	public void updateByAdmin(String email, String phone, String nickname, Boolean isPublic, UserRoleEnum role) {
		updateInfo(email, phone, nickname, isPublic);
		if (role != null) {
			this.role = role;
		}
	}

	public void updatePassword(String newEncodedPassword) {
		this.password = newEncodedPassword;
	}

	public void softDelete(String deletedBy) {
		super.softDelete(deletedBy);
		addresses.forEach(address -> address.softDelete(deletedBy));
	}

}
