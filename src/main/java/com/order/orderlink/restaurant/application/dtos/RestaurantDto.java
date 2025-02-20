package com.order.orderlink.restaurant.application.dtos;

import com.order.orderlink.food.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {
    private UUID restaurantId;
    private String name;
    private String address;
    private String phone;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean businessStatus;
    private String ownerName;
    private String businessRegNum;
    private Double avgRating;
    private Double ratingSum;
    private Integer ratingCount;
    private String region;
    private String categories;
    private List<RestaurantFoodDto> foods;
}
