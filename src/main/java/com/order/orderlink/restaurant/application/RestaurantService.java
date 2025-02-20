package com.order.orderlink.restaurant.application;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.restaurant.application.dtos.RestaurantRequest;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

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

    // 사용자 권한 조회
    private UserRoleEnum getUserRole(User user) {
        return user.getRole();
    }
}
