package com.order.orderlink.restaurant.domain;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.food.domain.Food;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_restaurants")
@EntityListeners(AuditingEntityListener.class)
public class Restaurant extends BaseTimeEntity {

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private UUID id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "address", unique = true, nullable = false)
	private String address;

	@Column(name = "phone", unique = true, nullable = false)
	private String phone;

	@Column(name = "description")
	private String description;

	@NotBlank
	@Column(name = "open_Time", nullable = false)
	private LocalTime openTime;

	@NotBlank
	@Column(name = "close_Time", nullable = false)
	private LocalTime closeTime;

	@Builder.Default
	@Column(name = "business_status", nullable = false)
	private boolean businessStatus = true;

	@Column(name = "owner_name", nullable = false)
	private String ownerName;

	@Column(name = "business_reg_num", unique = true, nullable = false)
	private String businessRegNum;

	@Builder.Default
	@Column(name = "avg_rating", nullable = false)
	private Double avgRating = 0.0;

	@Builder.Default
	@Column(name = "rating_sum", nullable = false)
	private Double ratingSum = 0.0;

	@Builder.Default
	@Column(name = "rating_count", nullable = false)
	private Integer ratingCount = 0;

	@Column(name = "region_id", nullable = false)
	private String regionId; // 추후 String -> UUID 변경 예정

	@Column(name = "categories_id", nullable = false)
	private String categoriesId; // 추후 String -> UUID 변경 예정

	@OneToMany(mappedBy = "restaurant")
	private List<Food> foods = new ArrayList<>(); // set 메서드 추가 -> Food 연관관계 설정

	public Restaurant(String name, String address, String phone, String description, LocalTime openTime, LocalTime closeTime, String ownerName, String businessRegNum, String regionId, String categoriesId) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.description = description;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.businessStatus = true;
		this.ownerName = ownerName;
		this.businessRegNum = businessRegNum;
		this.regionId = regionId;
		this.categoriesId = categoriesId;
	}
}