package com.order.orderlink.food.application;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.order.orderlink.common.client.RestaurantClient;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.FoodException;
import com.order.orderlink.common.exception.UserException;
import com.order.orderlink.common.external.s3.S3Service;
import com.order.orderlink.food.application.dtos.FoodDTO;
import com.order.orderlink.food.application.dtos.FoodRequest;
import com.order.orderlink.food.application.dtos.FoodResponse;
import com.order.orderlink.food.domain.Food;
import com.order.orderlink.food.domain.repository.FoodRepository;
import com.order.orderlink.restaurant.domain.Restaurant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {

	private final FoodRepository foodRepository;
	private final S3Service s3Service;
	private final RestaurantClient restaurantClient;

	public FoodResponse.Create createFood(FoodRequest.Create request, UUID restaurantId, UUID userId) {
		Restaurant restaurant = restaurantClient.getRestaurant(restaurantId);
		Food food = Food.builder()
			.restaurant(restaurant)
			.userId(userId)
			.name(request.getName())
			.description(request.getDescription())
			.price(request.getPrice())
			.isHidden(request.isHidden())
			.build();

		Food savedFood = foodRepository.save(food);

		return new FoodResponse.Create(savedFood.getId());
	}

	public FoodResponse.Update updateFood(UUID foodId, FoodRequest.Update request, UUID userId) {
		Food food = getFood(foodId);

		if (!food.getUserId().equals(userId)) {
			throw new UserException(ErrorCode.USER_ACCESS_DENIED);
		}

		food.updateFood(
			request.getName(),
			request.getDescription(),
			request.getPrice(),
			request.isHidden()
		);

		Food updatedFood = foodRepository.save(food);

		return new FoodResponse.Update(
			updatedFood.getId(),
			updatedFood.getName(),
			updatedFood.getDescription(),
			updatedFood.getPrice(),
			updatedFood.getIsHidden(),
			updatedFood.getUpdatedAt()
		);

	}

	public FoodResponse.Delete softDeleteFood(UUID foodId, UUID userId, String username) {
		Food food = getFood(foodId);

		if (!food.getUserId().equals(userId)) {
			throw new UserException(ErrorCode.USER_ACCESS_DENIED);
		}

		food.softDelete(username);

		return new FoodResponse.Delete(food.getUserId(), food.getDeletedAt());
	}

	private Food getFood(UUID foodId) {
		Food food = foodRepository.findById(foodId)
			.orElseThrow(() -> new FoodException(ErrorCode.FOOD_NOT_FOUND));
		return food;
	}

	public FoodResponse.UploadImage uploadFoodImage(UUID foodId, MultipartFile file) {
		String imageUrl = s3Service.uploadFile(file);
		Food food = getFood(foodId);
		food.setImageUrl(imageUrl);
		return new FoodResponse.UploadImage(imageUrl);
	}

	@Transactional(readOnly = true)
	public FoodResponse.GetFoodDetail getFoodDetail(UUID foodId) {
		Food food = getFood(foodId);
		return FoodResponse.GetFoodDetail.builder()
			.name(food.getName())
			.description(food.getDescription())
			.price(food.getPrice())
			.imageUrl(food.getImageUrl())
			.isHidden(food.getIsHidden())
			.createdAt(food.getCreatedAt())
			.updatedAt(food.getUpdatedAt())
			.build();
	}

	@Transactional(readOnly = true)
	public FoodResponse.GetFoods getFoods(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Food> foodPage = foodRepository.findAll(pageable);

		List<FoodDTO> foodDTOs = foodPage.getContent().stream()
			.map(food -> FoodDTO.builder()
				.foodId(food.getId())
				.name(food.getName())
				.price(food.getPrice())
				.imageUrl(food.getImageUrl())
				.isHidden(food.getIsHidden())
				.build())
			.toList();

		return new FoodResponse.GetFoods(foodDTOs, foodPage.getTotalPages(), foodPage.getNumber() + 1);

	}

}
