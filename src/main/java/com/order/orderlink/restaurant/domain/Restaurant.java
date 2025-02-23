package com.order.orderlink.restaurant.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.orderlink.common.auth.util.LocalTimeToStringConverter;
import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.food.domain.Food;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address", unique = true, nullable = false)
	private String address;

	@Column(name = "phone", unique = true, nullable = false)
	private String phone;

	@Column(name = "description")
	private String description;

	@Convert(converter = LocalTimeToStringConverter.class)
	@Column(name = "open_time", nullable = false)
	private LocalTime openTime;

	@Convert(converter = LocalTimeToStringConverter.class)
	@Column(name = "close_time", nullable = false)
	private LocalTime closeTime;

	@Builder.Default
	@Column(name = "business_status", nullable = false)
	private boolean businessStatus = true;

	@Column(name = "owner_name", nullable = false)
	private String ownerName;

    @Column(name = "owner_auth_token", unique = true, nullable = false)
    private String ownerAuthToken;

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

	@OneToMany(mappedBy = "restaurant")
	@JsonIgnore
	private List<Food> foods = new ArrayList<>();

	private UUID regionId;

	public void updateRatingSum(Double newRatingSum) {
		this.ratingSum = newRatingSum;
	}

	public void updateRatingCount(Integer newRatingCount) {
		this.ratingCount = newRatingCount;
	}

	public void updateAvgRating(Double newAvgRating) {
		this.avgRating = newAvgRating;
	}

	// 음식점 정보 수정 메서드
    public void update(String name, String address, String phone, String description, String openTime, String closeTime, String ownerName, String businessRegNum) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        if (name != null && !name.isBlank()) {
            this.name = name;
        }

        if (address != null && !address.isBlank()) {
            this.address = address;
        }

        if (phone != null && !phone.isBlank()) {
            this.phone = phone;
        }

        if (description != null && !description.isBlank()) {
            this.description = description;
        }

        if (openTime != null && !openTime.isBlank()) {
            this.openTime = LocalTime.parse(openTime, formatter);
        }

        if (closeTime != null && !closeTime.isBlank()) {
            this.closeTime = LocalTime.parse(closeTime, formatter);
        }

        if (ownerName != null && !ownerName.isBlank()) {
            this.ownerName = ownerName;
        }

        if (businessRegNum != null && !businessRegNum.isBlank()) {
            this.businessRegNum = businessRegNum;
        }
    }

	// Soft Delete 메서드
	public void softDelete(String deletedBy) {
		super.softDelete(deletedBy);
		foods.forEach(food -> food.softDelete(deletedBy));
	}
}