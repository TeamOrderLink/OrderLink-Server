package com.order.orderlink.food.presentation;

import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.food.application.FoodService;
import com.order.orderlink.food.application.dtos.FoodRequest;
import com.order.orderlink.food.application.dtos.FoodResponse;
import jakarta.validation.Valid;
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
            @Valid @RequestBody FoodRequest.Create request) {
        return SuccessResponse.success(SuccessCode.FOOD_CREATE_SUCCESS, foodService.createFood(request));
    }

    @PutMapping("/{foodId}")
    public SuccessResponse<FoodResponse.Update> updateFood(
            @PathVariable UUID foodId,
            @RequestBody FoodRequest.Update request) {
        return SuccessResponse.success(SuccessCode.FOOD_UPDATE_SUCCESS, foodService.updateFood(foodId, request));
    }

}
