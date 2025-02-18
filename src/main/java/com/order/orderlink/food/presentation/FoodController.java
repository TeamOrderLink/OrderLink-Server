package com.order.orderlink.food.presentation;

import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.food.application.FoodService;
import com.order.orderlink.food.application.dtos.FoodRequest;
import com.order.orderlink.food.application.dtos.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public SuccessResponse<FoodResponse.Create> createFood(
            @RequestParam UUID restaurantId,
            @RequestBody FoodRequest.Create request) {
        return SuccessResponse.success(SuccessCode.FOOD_CREATE_SUCCESS, foodService.createFood(restaurantId, request));
    }

}
