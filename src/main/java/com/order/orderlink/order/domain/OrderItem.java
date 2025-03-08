package com.order.orderlink.order.domain;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "p_order_items")
public class OrderItem extends BaseTimeEntity {

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@NotNull
	private String foodName;

	@NotNull
	private Integer quantity;

	@NotNull
	private Integer price;

	public void setOrder(Order order) {
		this.order = order;
	}
}
