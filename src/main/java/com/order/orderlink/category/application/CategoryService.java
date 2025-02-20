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
import com.order.orderlink.category.domain.RestaurantCategory;
import com.order.orderlink.category.domain.repository.JpaCategoryRepository;
import com.order.orderlink.category.domain.repository.JpaRestaurantCategoryRepository;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.CategoryException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final JpaCategoryRepository categoryRepository;
	private final JpaRestaurantCategoryRepository restaurantCategoryRepository;

	public CategoryResponse.Create createCategory(CategoryRequest.Create request) {
		Category category = Category.builder()
			.name(request.getName())
			.build();
		categoryRepository.save(category);
		return CategoryResponse.Create.builder().categoryId(category.getId()).build();
	}

	public void createRestaurantCategory(CategoryRequest.RegisterRestaurantCategory request, UUID restaurantId) {
		List<UUID> categoryIds = request.getCategoryIds();
		for (UUID categoryId : categoryIds) {
			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
			RestaurantCategory restaurantCategory = RestaurantCategory.builder()
				.categoryId(category.getId())
				.restaurantId(restaurantId)
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
}
