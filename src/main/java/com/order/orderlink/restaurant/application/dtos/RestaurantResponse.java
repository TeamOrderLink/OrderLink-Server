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
    // 음식점 등록 Response
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        private final UUID restaurantId;
        private final String ownerAuthToken;
    }

    // 전체 음식점 조회 Response
    @Getter
    @Builder
    @AllArgsConstructor
    public static class GetRestaurants {
        private final List<RestaurantDto> restaurants;
    }

    // 해당 카테고리로 조회한 음식점 리스트 Response
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RestaurantsByCategory {
        List<RestaurantDto> restaurantsByCategory;
    }

    // 음식점 DTO(foods 미포함) : 전체 음식점 조회 시 리스트에 담을 DTO
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

    // 음식점 상세 Response(foods 포함)
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

    // 음식 DTO : 음식점 상세 조회 시 음식 리스트에 담을 DTO
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

    // 음식점 수정 Response : 수정이 가능한 필드만 반환
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

    // 음식점 삭제 Response
    @Getter
    @AllArgsConstructor
    public static class Delete {
        private UUID restaurantId;
        private LocalDateTime deletedAt;
    }
}
