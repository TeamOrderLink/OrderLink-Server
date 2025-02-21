package com.order.orderlink.restaurant.presentation;

import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.restaurant.application.RestaurantService;
import com.order.orderlink.restaurant.application.dtos.RestaurantRequest;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 음식점 등록 API
    @PostMapping
    public SuccessResponse<RestaurantResponse.Create> createRestaurant(
        @RequestBody RestaurantRequest.Create request) {
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
    public SuccessResponse<RestaurantResponse.GetRestaurant> getRestaurant(@PathVariable("id") UUID restaurantId) {
        return SuccessResponse.success(SuccessCode.RESTAURANT_GET_SUCCESS,
                restaurantService.getRestaurant(restaurantId));
    }
}
