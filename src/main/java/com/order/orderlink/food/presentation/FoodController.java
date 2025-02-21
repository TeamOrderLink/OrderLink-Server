package com.order.orderlink.food.presentation;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.food.application.FoodService;
import com.order.orderlink.food.application.dtos.FoodRequest;
import com.order.orderlink.food.application.dtos.FoodResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public SuccessResponse<FoodResponse.Create> createFood(
            @Valid @RequestBody FoodRequest.Create request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID userId = userDetails.getUser().getId();
        return SuccessResponse.success(SuccessCode.FOOD_CREATE_SUCCESS, foodService.createFood(request, userId));
    }

    @PutMapping("/{foodId}")
    public SuccessResponse<FoodResponse.Update> updateFood(
            @PathVariable UUID foodId,
            @RequestBody FoodRequest.Update request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID userId = userDetails.getUser().getId();
        return SuccessResponse.success(SuccessCode.FOOD_UPDATE_SUCCESS, foodService.updateFood(foodId, request, userId));
    }

    @DeleteMapping("/{foodId}")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public SuccessResponse<FoodResponse.Delete> deleteFood(
            @PathVariable UUID foodId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getUser().getId();
        return SuccessResponse.success(SuccessCode.FOOD_DELETE_SUCCESS, foodService.softDeleteFood(foodId, userId));
    }

}
