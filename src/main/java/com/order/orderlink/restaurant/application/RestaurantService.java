package com.order.orderlink.restaurant.application;

import com.order.orderlink.common.auth.util.TokenGenerator;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.common.exception.RestaurantException;
import com.order.orderlink.food.domain.Food;
import com.order.orderlink.restaurant.application.dtos.RestaurantRequest;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse.GetRestaurant;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse.GetRestaurantFoodDto;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse.RestaurantDto;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // 음식점 등록 API
    public RestaurantResponse.Create createRestaurant(RestaurantRequest.Create request) {
        // 점주 인증 토큰 생성
        String ownerAuthToken = TokenGenerator.generateToken();

        // Request DTO -> Entity
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .description(request.getDescription())
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .ownerAuthToken(ownerAuthToken)
                .ownerName(request.getOwnerName())
                .businessRegNum(request.getBusinessRegNum())
                .build();

        // 음식점 repository 저장
        restaurantRepository.save(restaurant);

        // 응답 결과 반환
        return new RestaurantResponse.Create(restaurant.getId(), ownerAuthToken);
    }

    // 음식점 정보 수정 API
    public RestaurantResponse.Update updateRestaurant(RestaurantRequest.Update request, UUID restaurantId) {
        // 해당 ID의 음식점 정보 조회
        Restaurant restaurant = getRestaurantById(restaurantId);

        // 해당 음식점의 주인이 현재 요청한 사용자가 맞는지 검증 -> OWNER 권한 & 음식점 등록 시 전달한 인증 키
        // 일치하면 수정 작업 진행, 일치하지 않으면 Exception
        if (!request.getOwnerAuthToken().equals(restaurant.getOwnerAuthToken())) {
            throw new AuthException(ErrorCode.TOKEN_INVALID);
        }

        // 해당 음식점 정보 수정
        restaurant.update(
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getDescription(),
                request.getOpenTime(),
                request.getCloseTime(),
                request.getOwnerName(),
                request.getOwnerAuthToken(),
                request.getBusinessRegNum()
        );

        // 변경 내용 DB에 저장
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponse.Update.builder()
                .restaurantId(updatedRestaurant.getId())
                .name(updatedRestaurant.getName())
                .address(updatedRestaurant.getAddress())
                .phone(updatedRestaurant.getPhone())
                .description(updatedRestaurant.getDescription())
                .openTime(updatedRestaurant.getOpenTime())
                .closeTime(updatedRestaurant.getCloseTime())
                .businessStatus(isOpen(updatedRestaurant.getOpenTime(), updatedRestaurant.getCloseTime()))
                .ownerName(updatedRestaurant.getOwnerName())
                .businessRegNum(updatedRestaurant.getBusinessRegNum())
                .build();
    }

    // 음식점 조회 API
    @Transactional(readOnly = true)
    public RestaurantResponse.GetRestaurant getRestaurant(UUID restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);

        boolean isOpen = isOpen(restaurant.getOpenTime(), restaurant.getCloseTime());

        return GetRestaurant.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .description(restaurant.getDescription())
                .openTime(String.valueOf(restaurant.getOpenTime()))
                .closeTime(String.valueOf(restaurant.getCloseTime()))
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

    // 전체 음식점 조회 API
    @Transactional(readOnly = true)
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
        boolean isOpen = isOpen(restaurant.getOpenTime(), restaurant.getCloseTime());

        return RestaurantDto.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .description(restaurant.getDescription())
                .openTime(String.valueOf(restaurant.getOpenTime()))
                .closeTime(String.valueOf(restaurant.getCloseTime()))
                .businessStatus(isOpen)
                .ownerName(restaurant.getOwnerName())
                .businessRegNum(restaurant.getBusinessRegNum())
                .avgRating(restaurant.getAvgRating())
                .ratingSum(restaurant.getRatingSum())
                .ratingCount(restaurant.getRatingCount())
                .build();
    }

    // Convert : Food -> RestaurantFoodDto
    private GetRestaurantFoodDto convertToRestaurantFoodDto(Food food) {
        return GetRestaurantFoodDto.builder()
                .foodId(food.getId())
                .foodName(food.getName())
                .foodDescription(food.getDescription())
                .price(food.getPrice())
                .imageUrl(food.getImageUrl())
                .build();
    }

    // 영업 상태 확인
    private boolean isOpen(LocalTime openTime, LocalTime closeTime) {
        LocalTime now = LocalTime.now();
        return now.isAfter(openTime) && now.isBefore(closeTime);
    }

    // 음식점 찾기
	@Transactional(readOnly = true)
	public Restaurant getRestaurantById(UUID restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new RestaurantException(ErrorCode.RESTAURANT_NOT_FOUND));
	}
}
