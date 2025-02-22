package com.order.orderlink.review.domain;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.restaurant.domain.Restaurant;

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
@Table(name = "p_reviews")
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "order_id", nullable = false, unique = true)
	private UUID orderId;

	@Column(name = "rating", nullable = false)
	private Integer rating;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Builder
	public Review(Restaurant restaurant, UUID userId, UUID orderId, Integer rating, String content) {
		this.restaurant = restaurant;
		this.userId = userId;
		this.orderId = orderId;
		this.rating = rating;
		this.content = content;
	}

	public void update(Integer newRating, String newContent) {
		if (newRating != null) {
			this.rating = newRating;
		}
		if (newContent != null && !newContent.trim().isEmpty()) {
			this.content = newContent;
		}
	}

	public void softDelete(String deletedBy) {
		super.softDelete(deletedBy);
	}

}
