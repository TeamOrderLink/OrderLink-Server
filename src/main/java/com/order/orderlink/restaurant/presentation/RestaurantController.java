package com.order.orderlink.restaurant.presentation;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.restaurant.application.RestaurantService;
import com.order.orderlink.restaurant.application.dtos.RestaurantRequest;
import com.order.orderlink.restaurant.application.dtos.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 음식점 등록 API
    @PostMapping
    public SuccessResponse<RestaurantResponse.Create> createRestaurant(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody RestaurantRequest.Create request) {
        return SuccessResponse.success(SuccessCode.RESTAURNT_CREATE_SUCCESS,
                restaurantService.createRestaurant(userDetails, request));
    }
}
