package com.order.orderlink.category.domain;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_restaurant_categories")
@EntityListeners(AuditingEntityListener.class)
public class RestaurantCategory extends BaseTimeEntity {
	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

}
