package com.order.orderlink.restaurant.application.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class RestaurantRequest {

    @Getter
    public static class Create {
        @NotBlank
        private String name;

        @NotBlank
        private String address;

        @NotBlank
        private String phone;

        private String description;

        @NotBlank
        private String businessHours;

        @NotBlank
        private String ownerName;

        @NotBlank
        private String businessRegNum;

        @NotBlank
        private UUID categoriesId;

        @NotBlank
        private UUID regionId;

        @NotBlank
        private List<RestaurantFoodDto> foods;
    }
}
