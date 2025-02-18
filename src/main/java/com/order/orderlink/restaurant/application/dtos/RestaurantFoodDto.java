package com.order.orderlink.restaurant.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantFoodDto {
    private String foodName;
    private String foodDescription;
    private Long price;
    private String imageUrl;
}
