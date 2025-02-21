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
    public static class Create {
        private final UUID id;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Update {
        private final UUID id;
        private final String name;
        private final String description;
        private final int price;
        private final String imageUrl;
        private final boolean isHidden;
        private final LocalDateTime updatedAt;
        private final String updatedBy;
    }

    @Getter
    @AllArgsConstructor
    public static class Delete {
        private final UUID id;
        private final LocalDateTime deletedAt;
    }


}
