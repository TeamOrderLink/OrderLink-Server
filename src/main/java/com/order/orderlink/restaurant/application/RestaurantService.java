package com.order.orderlink.restaurant.application;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.enums.ErrorCode;
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

	// 음식점 등록 메서드
	public RestaurantResponse.Create createRestaurant(RestaurantRequest.Create request) {

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
			.build();

		// 음식점 repository 저장
		restaurantRepository.save(restaurant);

		// 응답 결과 반환
		return new RestaurantResponse.Create(restaurant.getId());
	}

	/** restaurantRepository.findAll();로 가져온 리스트를 매핑이 끝날 때 까지 세션 유지하기 위해 트랜잭션 사용 **/
	// 음식점 조회 API
	@Transactional(readOnly = true)
	public GetRestaurant getRestaurant(UUID restaurantId) {
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

	/** restaurantRepository.findAll();로 가져온 리스트를 매핑이 끝날 때 까지 세션 유지하기 위해 트랜잭션 사용 **/
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

	@Transactional(readOnly = true)
	public Restaurant getRestaurantById(UUID restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new RestaurantException(ErrorCode.RESTAURANT_NOT_FOUND));
	}
}
