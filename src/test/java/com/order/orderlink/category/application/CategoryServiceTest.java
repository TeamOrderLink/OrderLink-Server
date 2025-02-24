package com.order.orderlink.category.application;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.category.application.dtos.CategoryRequest;
import com.order.orderlink.category.domain.Category;
import com.order.orderlink.category.domain.RestaurantCategory;
import com.order.orderlink.category.domain.repository.CategoryRepository;
import com.order.orderlink.category.domain.repository.RestaurantCategoryRepository;
import com.order.orderlink.common.auth.util.JwtUtil;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
public class CategoryServiceTest {
	@MockitoBean
	JwtUtil jwtUtil;

	@Autowired
	CategoryService categoryService;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RestaurantCategoryRepository restaurantCategoryRepository;

	List<UUID> categoryIds = new ArrayList<>();

	@BeforeEach
	void setUp() {
		Category category = Category.builder()
			.name("한식")
			.build();
		categoryRepository.save(category);
		Category category2 = Category.builder()
			.name("중식")
			.build();
		categoryRepository.save(category2);
		categoryIds.add(category.getId());
		categoryIds.add(category2.getId());

	}

	@DisplayName("음식점 카테고리 등록성공")
	@Test
	void registerRestaurantCategorySuccess() {
		//given
		UUID restaurantId = UUID.randomUUID();
		CategoryRequest.RegisterRestaurantCategory request = CategoryRequest.RegisterRestaurantCategory
			.builder()
			.categoryIds(categoryIds)
			.build();
		//when
		categoryService.createRestaurantCategory(request, restaurantId);

		//then
		List<RestaurantCategory> restaurantCategories = restaurantCategoryRepository.findAll();
		assertThat(restaurantCategories).hasSize(2);
	}

	@DisplayName("카테고리 아이디가 없을때 등록 실패")
	@Test
	void registerRestaurantCategoryFail() {
		//given
		UUID restaurantId = UUID.randomUUID();
		CategoryRequest.RegisterRestaurantCategory request = CategoryRequest.RegisterRestaurantCategory
			.builder()
			.categoryIds(new ArrayList<>())
			.build();
		//when
		categoryService.createRestaurantCategory(request, restaurantId);

		//then
		List<RestaurantCategory> restaurantCategories = restaurantCategoryRepository.findAll();
		assertThat(restaurantCategories).isEmpty();
	}
}
