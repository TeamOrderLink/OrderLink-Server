package com.order.orderlink.category.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.restaurant.domain.RestaurantCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "p_categories")
@EntityListeners(AuditingEntityListener.class)
public class Category extends BaseTimeEntity {
	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private UUID id;

	@NotNull
	private String name;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RestaurantCategory> restaurantCategories = new ArrayList<>();

	public void updateName(String name) {
		this.name = name;
	}

	public static Category create(String name) {
		Category category = new Category();
		category.name = name;
		return category;
	}
}

