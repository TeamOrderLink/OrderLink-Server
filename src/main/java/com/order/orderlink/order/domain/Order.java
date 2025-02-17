package com.order.orderlink.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.orderitem.domain.OrderItem;
import com.order.orderlink.payment.domain.Payment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "p_orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseTimeEntity {

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private UUID id;

	@NotNull
	private UUID userId;

	@NotNull
	private UUID restaurantId;

	@NotNull
	private String deliveryAddress;

	private String deliveryRequest;

	@NotNull
	private Integer totalPrice;

	@NotNull
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@NotNull
	@Enumerated(EnumType.STRING)
	private OrderType orderType;

	@Builder.Default
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Payment payment;

	public void addOrderItem(OrderItem orderItem) {
		orderItem.setOrder(this);
		orderItems.add(orderItem);
	}
}
