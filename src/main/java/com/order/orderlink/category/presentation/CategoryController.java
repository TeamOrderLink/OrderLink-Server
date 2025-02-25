package com.order.orderlink.category.presentation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.category.application.CategoryService;
import com.order.orderlink.category.application.dtos.CategoryRequest;
import com.order.orderlink.category.application.dtos.CategoryResponse;
import com.order.orderlink.common.dtos.SuccessNonDataResponse;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<CategoryResponse.Create> createCategory(
		@Valid @RequestBody CategoryRequest.Create request) {
		return SuccessResponse.success(SuccessCode.CATEGORY_CREATE_SUCCESS,
			categoryService.createCategory(request));
	}

	@PostMapping("/register")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessNonDataResponse createRestaurantCategory(
		@RequestBody CategoryRequest.RegisterRestaurantCategory request,
		@RequestParam UUID restaurantId
	) {
		categoryService.createRestaurantCategory(request, restaurantId);
		return SuccessNonDataResponse.success(SuccessCode.CATEGORY_REGISTER_SUCCESS);
	}

	@GetMapping
	public SuccessResponse<CategoryResponse.GetCateories> getCategories(
	) {
		return SuccessResponse.success(SuccessCode.CATEGORY_GET_SUCCESS,
			categoryService.getCategories());
	}

	@DeleteMapping("/{categoryId}")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessNonDataResponse getCategories(
		@PathVariable UUID categoryId
	) {
		categoryService.deleteCategory(categoryId);
		return SuccessNonDataResponse.success(SuccessCode.CATEGORY_DELETE_SUCCESS);
	}

	@PatchMapping("/{categoryId}")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessNonDataResponse updateCategories(
		@PathVariable UUID categoryId,
		@Valid @RequestBody CategoryRequest.UpdateCategory request
	) {
		categoryService.updateCategory(categoryId, request);
		return SuccessNonDataResponse.success(SuccessCode.CATEGORY_UPDATE_SUCCESS);
	}
}
