package com.order.orderlink.restaurant.application.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RestaurantDto {
        private UUID restaurantId;
        private String name;
        private String address;
        private String phone;
        private String description;

        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "영업 시작 시간은 00:00 ~ 23:59 이내의 시간으로 HH:mm 형식에 맞게 입력해 주세요.")
        private String openTime;

        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "영업 종료 시간은 00:00 ~ 23:59 이내의 시간으로 HH:mm 형식에 맞게 입력해 주세요.")
        private String closeTime;

        private boolean businessStatus;
        private String ownerName;
        private String businessRegNum;
        private Double avgRating;
        private Double ratingSum;
        private Integer ratingCount;
        private List<RestaurantFoodDto> foods;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RestaurantFoodDto {
        private String foodName;
        private String foodDescription;
        private int price;
        private String imageUrl;
    }
}
