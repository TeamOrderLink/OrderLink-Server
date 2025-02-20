package com.order.orderlink.restaurant.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantFoodDto {
    private String foodName;
    private String foodDescription;
    private int price;
    private String imageUrl;
}
