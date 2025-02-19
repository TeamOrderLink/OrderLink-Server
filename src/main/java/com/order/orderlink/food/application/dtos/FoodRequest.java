package com.order.orderlink.food.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.util.UUID;

public class FoodRequest {

    @Getter
    public static class Create {

        @NotNull(message = "음식점 id를 입력해주세요.")
        private UUID restaurantId;

        @NotBlank(message = "음식명을 입력해주세요.")
        private String name;

        private String description;

        @NotNull(message = "가격을 입력해주세요.")
        @Positive(message = "가격은 0원 이상이여야 합니다.")
        private int price;

        private String imageUrl;

        private boolean isHidden;
    }
}
