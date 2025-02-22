package com.order.orderlink.review.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

	boolean existsByOrderId(UUID orderId);

	Page<Review> findByRestaurantId(UUID restaurantId, Pageable pageable);

}
