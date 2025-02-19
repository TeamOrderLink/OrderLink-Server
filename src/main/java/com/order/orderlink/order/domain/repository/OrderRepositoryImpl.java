package com.order.orderlink.order.domain.repository;

import static com.order.orderlink.order.domain.QOrder.*;
import static com.order.orderlink.orderitem.domain.QOrderItem.*;
import static com.order.orderlink.restaurant.domain.QRestaurant.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<Order> searchOrdersWithItems(OrderStatus status, String restaurantName, String foodName,
		LocalDate startDate, LocalDate endDate, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();

		if (status != null) {
			builder.and(order.status.eq(status));
		}

		if (restaurantName != null && !restaurantName.isEmpty()) {
			builder.and(restaurant.name.contains(restaurantName));
		}

		if (foodName != null && !foodName.isEmpty()) {
			builder.and(orderItem.foodName.contains(foodName));
		}

		// 날짜 범위 (CreatedAt)
		if (startDate != null) {
			builder.and(order.createdAt.goe(startDate.atStartOfDay())); //자정
		}
		if (endDate != null) {
			builder.and(order.createdAt.loe(endDate.atTime(23, 59, 59))); //하루끝
		}

		List<Order> orders = queryFactory.selectFrom(order)
			.join(order.orderItems, orderItem)
			.leftJoin(restaurant).on(restaurant.id.eq(order.restaurantId))
			.where(builder)
			.offset(pageable.getOffset()) // 페이징 시작점
			.limit(pageable.getPageSize()) // 페이징 크기
			.fetch();

		// 총 결과 수 계산
		long total = queryFactory.selectFrom(order)
			.join(order.orderItems, orderItem)
			.leftJoin(restaurant).on(restaurant.id.eq(order.restaurantId))
			.where(builder)
			.fetchCount();

		return new PageImpl<>(orders, pageable, total);
	}
}
