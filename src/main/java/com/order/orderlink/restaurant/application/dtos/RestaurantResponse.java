package com.order.orderlink.restaurant.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create{
        private final UUID restaurantId;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class GetRestaurants {
        private final List<RestaurantDto> restaurants;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GetRestaurant {
        private final RestaurantDto restaurantDto;
    }
}
