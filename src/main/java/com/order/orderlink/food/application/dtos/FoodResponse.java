package com.order.orderlink.food.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public class FoodResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class create {
        private UUID id;
        private UUID restaurantId;
        private String name;
        private String description;
        private int price;
        private String imageUrl;
        private boolean isHidden;
        private LocalDateTime createdAt;
        private String createdBy;
    }
}
