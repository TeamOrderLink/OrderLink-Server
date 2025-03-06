package com.order.orderlink.category.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.category.application.dtos.CategoryDTO;
import com.order.orderlink.category.application.dtos.CategoryRequest;
import com.order.orderlink.category.application.dtos.CategoryResponse;
import com.order.orderlink.category.domain.Category;
import com.order.orderlink.category.domain.repository.CategoryRepository;
import com.order.orderlink.category.exception.CategoryException;
import com.order.orderlink.common.client.RestaurantClient;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.restaurant.domain.RestaurantCategory;
import com.order.orderlink.restaurant.domain.repository.RestaurantCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final RestaurantCategoryRepository restaurantCategoryRepository;
	private final RestaurantClient restaurantClient;

	public CategoryResponse.Create createCategory(CategoryRequest.Create request) {
		Category category = Category.create(request.getName());
		categoryRepository.save(category);
		return CategoryResponse.Create.builder().categoryId(category.getId()).build();
	}

	public void createRestaurantCategory(CategoryRequest.RegisterRestaurantCategory request, UUID restaurantId) {
		List<UUID> categoryIds = request.getCategoryIds();
		for (UUID categoryId : categoryIds) {
			Category category = getCategory(categoryId);
			RestaurantCategory restaurantCategory = RestaurantCategory.builder()
				.category(category)
				.restaurant(restaurantClient.getRestaurant(restaurantId))
				.build();
			restaurantCategoryRepository.save(restaurantCategory);
		}
	}

	@Transactional(readOnly = true)
	public CategoryResponse.GetCateories getCategories() {
		List<Category> categories = categoryRepository.findAll();
		List<CategoryDTO> categoryDTOS = new ArrayList<>();
		for (Category category : categories) {
			CategoryDTO categoryDTO = CategoryDTO.builder().categoryId(category.getId())
				.name(category.getName())
				.createdAt(category.getCreatedAt())
				.build();
			categoryDTOS.add(categoryDTO);
		}
		return CategoryResponse.GetCateories.builder().categories(categoryDTOS).build();
	}

	public void deleteCategory(UUID categoryId) {
		Category category = getCategory(categoryId);
		restaurantCategoryRepository.deleteByCategoryId(category.getId());
		categoryRepository.deleteById(categoryId);
	}

	private Category getCategory(UUID categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	public void updateCategory(UUID categoryId, CategoryRequest.UpdateCategory request) {
		Category category = getCategory(categoryId);
		category.updateName(request.getName());
	}
}
