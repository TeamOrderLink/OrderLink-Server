package com.order.orderlink.restaurant.domain;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.food.domain.Food;
import com.order.orderlink.restaurant.application.dtos.RestaurantFoodDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

	@Column(name = "business_hours",nullable = false)
	private String businessHours;

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
	private UUID regionId;

	// 클라이언트 요청 시 입력한 여러 Food 데이터를 requestDTO에 매핑됨
	// Food와 연관관계를 양방향으로 안걸고 해당 음식점에서 판매하는 음식 리스트를 어떻게 담을 수 있을까?
	@OneToMany
	private List<Food> foods = new ArrayList<>();

	public Restaurant(String name, String address, String phone, String description, String businessHours, String ownerName, String businessRegNum, UUID regionId) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.description = description;
		this.businessHours = businessHours;
		this.businessStatus = true;
		this.ownerName = ownerName;
		this.businessRegNum = businessRegNum;
//		this.avgRating = 0.0;
//		this.ratingSum = 0.0;
//		this.ratingCount = 0;
		this.regionId = regionId;
	}
}