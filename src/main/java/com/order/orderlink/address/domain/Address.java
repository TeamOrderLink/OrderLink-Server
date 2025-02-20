package com.order.orderlink.address.domain;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_addresses")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "is_default", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDefault;

	@Builder
	public Address(User user, String address, Boolean isDefault) {
		this.user = user;
		this.address = address;
		this.isDefault = (isDefault != null) ? isDefault : false;
	}

	public void update(String address, Boolean isDefault) {
		if (address != null && !address.trim().isEmpty()) {
			this.address = address;
		}
		if (isDefault != null) {
			this.isDefault = isDefault;
		}
	}

}
