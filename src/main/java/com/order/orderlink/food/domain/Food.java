package com.order.orderlink.food.domain;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.restaurant.domain.Restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_foods")
public class Food extends BaseTimeEntity {

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price", nullable = false)
	private int price;

	@Setter
	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "is_hidden", nullable = false)
	private Boolean isHidden = false;

	public void updateFood(String name, String description, int price, Boolean isHidden) {
		if (name != null && !name.trim().isEmpty()) {
			this.name = name;
		}
		if (description != null && !description.trim().isEmpty()) {
			this.description = description;
		}
		if (price != 0) {
			this.price = price;
		}
		if (isHidden != null) {
			this.isHidden = isHidden;
		}
	}

	public static Food create(Restaurant restaurant, UUID userId, String name, String description, int price,
		boolean isHidden) {
		Food food = new Food();
		food.restaurant = restaurant;
		food.userId = userId;
		food.name = name;
		food.description = description;
		food.price = price;
		food.isHidden = isHidden;
		return food;
	}

	public void deleteFoodSoftly(String deletedBy) {
		super.softDelete(deletedBy);
	}

}