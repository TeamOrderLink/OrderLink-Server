package com.order.orderlink.category.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
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
import com.order.orderlink.category.domain.repository.CategoryRepository;
import com.order.orderlink.common.auth.util.JwtUtil;
import com.order.orderlink.common.client.RestaurantClient;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.RestaurantCategory;
import com.order.orderlink.restaurant.domain.repository.RestaurantCategoryRepository;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
public class CategoryServiceTest {
	@MockitoBean
	JwtUtil jwtUtil;
	@MockitoBean
	RestaurantClient restaurantClient;

	@Autowired
	CategoryService categoryService;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RestaurantCategoryRepository restaurantCategoryRepository;

	List<UUID> categoryIds = new ArrayList<>();
	UUID restaurantId = null;
	@Autowired
	private RestaurantRepository restaurantRepository;

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
		Restaurant restaurant = Restaurant.builder()
			.name("꼰미고")
			.address("서울시 강남구 ")
			.phone("01011112222")
			.description("마싯는 타코집")
			.openTime(LocalTime.now())
			.closeTime(LocalTime.now())
			.ownerAuthToken("ownerAuthToken")
			.ownerName("경린")
			.businessRegNum("001020120")
			.regionId(UUID.randomUUID())
			.build();
		restaurantRepository.save(restaurant);
		restaurantId = restaurant.getId();
		when(restaurantClient.getRestaurant(restaurant.getId())).thenReturn(restaurant);
	}

	@DisplayName("음식점 카테고리 등록성공")
	@Test
	void registerRestaurantCategorySuccess() {
		//given
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
