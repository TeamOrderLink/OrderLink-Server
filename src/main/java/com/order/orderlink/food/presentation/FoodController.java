package com.order.orderlink.food.presentation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.food.application.FoodService;
import com.order.orderlink.food.application.dtos.FoodRequest;
import com.order.orderlink.food.application.dtos.FoodResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

	private final FoodService foodService;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_OWNER')")
	public SuccessResponse<FoodResponse.Create> createFood(
		@Valid @RequestBody FoodRequest.Create request,
		@RequestParam UUID restaurantId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.FOOD_CREATE_SUCCESS,
			foodService.createFood(request, restaurantId, userId));
	}

	@PutMapping("/{foodId}")
	@PreAuthorize("hasAuthority('ROLE_OWNER')")
	public SuccessResponse<FoodResponse.Update> updateFood(
		@PathVariable UUID foodId,
		@RequestBody FoodRequest.Update request,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.FOOD_UPDATE_SUCCESS,
			foodService.updateFood(foodId, request, userId));
	}

	@DeleteMapping("/{foodId}")
	@PreAuthorize("hasAuthority('ROLE_OWNER')")
	public SuccessResponse<FoodResponse.Delete> deleteFood(
		@PathVariable UUID foodId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UUID userId = userDetails.getUser().getId();
		String username = userDetails.getUser().getUsername();
		return SuccessResponse.success(SuccessCode.FOOD_DELETE_SUCCESS,
			foodService.softDeleteFood(foodId, userId, username));
	}

	@PostMapping("/{foodId}/uploadImg")
	@PreAuthorize("hasAuthority('ROLE_OWNER')")
	public SuccessResponse<FoodResponse.UploadImage> uploadImage(
		@PathVariable UUID foodId,
		@RequestParam("file") MultipartFile file
	) {
		return SuccessResponse.success(SuccessCode.FOOD_UPLOAD_IMG_SUCCESS, foodService.uploadFoodImage(foodId, file));
	}

	@GetMapping("/{foodId}")
	public SuccessResponse<FoodResponse.GetFoodDetail> getFoodDetail(
		@PathVariable UUID foodId) {

		return SuccessResponse.success(SuccessCode.FOOD_GET_SUCCESS,
			foodService.getFoodDetail(foodId));
	}

	@GetMapping
	public SuccessResponse<FoodResponse.GetFoods> getFoods(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam UUID restaurantId
	) {

		return SuccessResponse.success(SuccessCode.FOOD_GET_LIST_SUCCESS,
			foodService.getFoods(restaurantId, page, size));
	}
}
