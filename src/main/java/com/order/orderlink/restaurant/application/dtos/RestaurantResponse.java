package com.order.orderlink.restaurant.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
}
