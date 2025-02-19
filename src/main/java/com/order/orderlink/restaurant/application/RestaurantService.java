package com.order.orderlink.restaurant.application;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.food.domain.Food;
import com.order.orderlink.restaurant.application.dtos.RestaurantFoodDto;
import com.order.orderlink.restaurant.application.dtos.RestaurantRequest;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantResponse.Create createRestaurant(UserDetailsImpl userDetails, RestaurantRequest.Create request) {
        // 사용자 확인
        UUID userId = userDetails.getUser().getId();

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
                .regionId(request.getCategories())
                .categoriesId(request.getCategories())
                .foods(mapToFoodList(request.getFoods()))
                .build();

        // 음식점 repository 저장
        restaurantRepository.save(restaurant);

        // 응답 결과 반환
        return new RestaurantResponse.Create(restaurant.getId());
    }

    // List<RestaurantFoodDto> -> List<Food> 매핑 메서드
    private List<Food> mapToFoodList(List<RestaurantFoodDto> restaurantFoodDto) {
        return restaurantFoodDto.stream()
                .map(dto -> Food.builder()
                        .name(dto.getFoodName())
                        .description(dto.getFoodDescription())
                        .price(dto.getPrice())
                        .imageUrl(dto.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }


    // 사용자 권한 확인
    private UserRoleEnum getUserRole(User user) {
        return user.getRole();
    }


}
