package com.order.orderlink.restaurant.application;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.food.domain.Food;
import com.order.orderlink.restaurant.application.dtos.RestaurantDto;
import com.order.orderlink.restaurant.application.dtos.RestaurantFoodDto;
import com.order.orderlink.restaurant.application.dtos.RestaurantRequest;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // 음식점 등록 메서드
    public RestaurantResponse.Create createRestaurant(UserDetailsImpl userDetails, RestaurantRequest.Create request) {
        // 사용자 권한 확인 -> MASTER만 음식점 등록 가능
        if (!getUserRole(userDetails.getUser()).equals(UserRoleEnum.MASTER)) {
            throw new AuthException(ErrorCode.USER_ACCESS_DENIED);
        }

        // Request DTO -> Entity
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .description(request.getDescription())
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .ownerName(request.getOwnerName())
                .businessRegNum(request.getBusinessRegNum())
                .region(request.getRegion())
                .categories(request.getCategories())
                .build();

        // 음식점 repository 저장
        restaurantRepository.save(restaurant);


        // 응답 결과 반환
        return new RestaurantResponse.Create(restaurant.getId());
    }

    /**
        restaurantRepository.findAll();로 가져온 리스트를 매핑이 끝날 때 까지 세션 유지하기 위해 트랜잭션 사용
    */
    ///  전체 음식점 조회 메서드
    @Transactional
    public RestaurantResponse.GetRestaurants getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        // Convert : List<Restaurant> -> List<RestaurantDto>
        List<RestaurantDto> restaurantDtos = restaurants.stream()
                .map(this::convertToRestaurantDto)
                .collect(Collectors.toList());

        return RestaurantResponse.GetRestaurants.builder()
                .restaurants(restaurantDtos)
                .build();
    }

    // Convert : Restaurant -> RestaurantDto
    private RestaurantDto convertToRestaurantDto(Restaurant restaurant) {
        LocalTime now = LocalTime.now();
        boolean isOpen = now.isAfter(restaurant.getOpenTime()) && now.isBefore(restaurant.getCloseTime());

        return RestaurantDto.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .description(restaurant.getDescription())
                .openTime(restaurant.getOpenTime())
                .closeTime(restaurant.getCloseTime())
                .businessStatus(isOpen)
                .ownerName(restaurant.getOwnerName())
                .businessRegNum(restaurant.getBusinessRegNum())
                .avgRating(restaurant.getAvgRating())
                .ratingSum(restaurant.getRatingSum())
                .ratingCount(restaurant.getRatingCount())
                .foods(restaurant.getFoods().stream()
                        .map(this::convertToRestaurantFoodDto)
                        .collect(Collectors.toList()))
                .build();
    }

    // Convert : Food -> RestaurantFoodDto
    private RestaurantFoodDto convertToRestaurantFoodDto(Food food) {
        return RestaurantFoodDto.builder()
                .foodName(food.getName())
                .foodDescription(food.getDescription())
                .price(food.getPrice())
                .imageUrl(food.getImageUrl())
                .build();
    }

    // 사용자 권한 조회
    private UserRoleEnum getUserRole(User user) {
        return user.getRole();
    }


}
