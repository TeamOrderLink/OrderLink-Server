package com.order.orderlink.restaurant.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.restaurant.application.RestaurantService;
import com.order.orderlink.restaurant.application.dtos.RestaurantRequest;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse;
import com.order.orderlink.restaurant.domain.Restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

	private final RestaurantService restaurantService;

	//webclient 용
	@GetMapping("/{restaurantId}/getRestaurant")
	public ResponseEntity<Restaurant> getRestaurantById(@PathVariable UUID restaurantId) {
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		return ResponseEntity.ok(restaurant);
	}

	// 음식점 등록 API
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<RestaurantResponse.Create> createRestaurant(
		@Valid @RequestBody RestaurantRequest.Create request) {

		return SuccessResponse.success(SuccessCode.RESTAURANT_CREATE_SUCCESS,
			restaurantService.createRestaurant(request));
	}

	// 전체 음식점 목록 조회 API
	@GetMapping
	public SuccessResponse<RestaurantResponse.GetRestaurants> getAllRestaurants() {

		return SuccessResponse.success(SuccessCode.RESTAURANTS_GET_SUCCESS,
			restaurantService.getAllRestaurants());
	}

	// 음식점 조회 API
	@GetMapping("/{id}")
	public SuccessResponse<RestaurantResponse.GetRestaurant> getRestaurant(
		@PathVariable("id") UUID restaurantId) {

		return SuccessResponse.success(SuccessCode.RESTAURANT_GET_SUCCESS,
			restaurantService.getRestaurant(restaurantId));
	}

	// 음식점 수정 API
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_OWNER', 'ROLE_MASTER')")
	public SuccessResponse<RestaurantResponse.Update> updateRestaurant(
		@PathVariable("id") UUID restaurantId,
		@Valid @RequestBody RestaurantRequest.Update request) {

		return SuccessResponse.success(SuccessCode.RESTAURANT_UPDATE_SUCCESS,
			restaurantService.updateRestaurant(request, restaurantId));
	}

	// 음식점 삭제 API
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<RestaurantResponse.Delete> deleteRestaurant(
		@PathVariable("id") UUID restaurantId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return SuccessResponse.success(SuccessCode.RESTAURANT_DELETE_SUCCESS,
			restaurantService.softDeleteRestaurant(restaurantId, userDetails));
	}
}
