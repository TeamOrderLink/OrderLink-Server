package com.order.orderlink.food.application.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

public class FoodRequest {

    @Getter
    public static class Create {

        @NotNull
        private UUID restaurantId;

        @NotNull
        private String name;

        private String description;

        @NotNull
        private int price;

        @NotNull
        private String imageUrl;

        private boolean isHidden = false;
    }
}
