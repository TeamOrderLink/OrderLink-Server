package com.order.orderlink.payment.domain;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.order.domain.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_payments")
@EntityListeners(AuditingEntityListener.class)
public class Payment extends BaseTimeEntity {
	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private UUID id;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@NotNull
	private String cardNumber;

	@NotNull
	private String cardHolder;

	@NotNull
	private String expiryDate;

	@NotNull
	private Integer amount;

	@NotNull
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	private String bank;

}
