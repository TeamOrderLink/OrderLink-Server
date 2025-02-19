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
        log.info("name = " + request.getName());
        log.info("address = " + request.getAddress());
        log.info("phone = " + request.getPhone());
        log.info("description = " + request.getDescription());
        log.info("businessHours = " + request.getOpenTime());
        log.info("businessHours = " + request.getCloseTime());
        log.info("ownerName = " + request.getOwnerName());
        log.info("businessRegNum = " + request.getBusinessRegNum());
        log.info("categories = " + request.getCategories());
        log.info("region = " + request.getRegion());
        // foods
        request.getFoods().stream().forEach(food -> {
            log.info("food.name = " + food.getFoodName());
            log.info("food.description = " + food.getFoodDescription());
            log.info("food.price = " + String.valueOf(food.getPrice()));
            log.info("food.imageUrl = " + food.getImageUrl());
        });

        return SuccessResponse.success(SuccessCode.RESTAURNT_CREATE_SUCCESS,
                restaurantService.createRestaurant(userDetails, request));
    }

    // 테스트
    @GetMapping
    public String test() {
        log.info("컨트롤러 GET 메서드로 요청 들어옴");
        return "요청에 응답";
    }
}
