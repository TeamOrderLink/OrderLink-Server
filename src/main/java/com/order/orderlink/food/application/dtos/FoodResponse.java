package com.order.orderlink.food.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

public class FoodResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        private final UUID id;
    }
}
