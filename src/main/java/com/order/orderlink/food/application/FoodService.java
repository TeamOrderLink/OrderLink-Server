package com.order.orderlink.food.application;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.FoodException;
import com.order.orderlink.common.exception.RestaurantException;
import com.order.orderlink.common.exception.UserException;
import com.order.orderlink.food.application.dtos.FoodRequest;
import com.order.orderlink.food.application.dtos.FoodResponse;
import com.order.orderlink.food.domain.Food;
import com.order.orderlink.food.domain.repository.FoodRepository;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {

    private final FoodRepository foodRepository;
    private final RestaurantRepository restaurantRepository;

    public FoodResponse.Create createFood(FoodRequest.Create request, UUID userId) {

        Restaurant restaurant = getRestaurant(request.getRestaurantId());
        Food food = Food.builder()
                .restaurant(restaurant)
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
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
                request.getImageUrl(),
                request.isHidden()
        );

        Food updatedFood = foodRepository.save(food);

        return new FoodResponse.Update(
                updatedFood.getId(),
                updatedFood.getName(),
                updatedFood.getDescription(),
                updatedFood.getPrice(),
                updatedFood.getImageUrl(),
                updatedFood.getIsHidden(),
                updatedFood.getUpdatedAt(),
                updatedFood.getUpdatedBy()
        );

    }

    public FoodResponse.Delete softDeleteFood(UUID foodId, UUID userId) {
        Food food = getFood(foodId);

        if (!food.getUserId().equals(userId)) {
            throw new UserException(ErrorCode.USER_ACCESS_DENIED);
        }

        food.softDelete(userId.toString());

        return new FoodResponse.Delete(food.getUserId(), food.getDeletedAt());
    }


    private Food getFood(UUID foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new FoodException(ErrorCode.FOOD_NOT_FOUND));
        return food;
    }

    private Restaurant getRestaurant(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantException(ErrorCode.RESTAURANT_NOT_FOUND));

        return restaurant;
    }

}
