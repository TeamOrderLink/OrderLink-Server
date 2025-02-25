package com.order.orderlink.restaurant.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        private final UUID restaurantId;
        private final String ownerAuthToken;
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
    @NoArgsConstructor
    public static class GetRestaurant {
        private UUID restaurantId;
        private String name;
        private String address;
        private String phone;
        private String description;
        private String openTime;
        private String closeTime;
        private boolean businessStatus;
        private String ownerName;
        private String businessRegNum;
        private Double avgRating;
        private Double ratingSum;
        private Integer ratingCount;
        private List<GetRestaurantFoodDto> foods;
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
        private String openTime;
        private String closeTime;
        private boolean businessStatus;
        private String ownerName;
        private String businessRegNum;
        private Double avgRating;
        private Double ratingSum;
        private Integer ratingCount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetRestaurantFoodDto {
        private UUID foodId;
        private String foodName;
        private String foodDescription;
        private int price;
        private String imageUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Update {
        private UUID restaurantId;
        private String name;
        private String address;
        private String phone;
        private String description;
        private String openTime;
        private String closeTime;
        private boolean businessStatus;
        private String ownerName;
        private String businessRegNum;
    }

    @Getter
    @AllArgsConstructor
    public static class Delete {
        private UUID restaurantId;
        private LocalDateTime deletedAt;
    }
}
