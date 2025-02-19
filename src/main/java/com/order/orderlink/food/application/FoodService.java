package com.order.orderlink.food.application;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.RestaurantException;
import com.order.orderlink.food.application.dtos.FoodRequest;
import com.order.orderlink.food.application.dtos.FoodResponse;
import com.order.orderlink.food.domain.Food;
import com.order.orderlink.food.domain.repository.FoodRepository;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final RestaurantRepository restaurantRepository;

    public FoodResponse.Create createFood(UUID restaurantId, FoodRequest.Create request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantException(ErrorCode.RESTAURANT_NOT_FOUND));
        Food food = Food.builder()
                .restaurant(restaurant)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .isHidden(request.isHidden())
                .build();

        Food savedFood = foodRepository.save(food);

        return new FoodResponse.Create(savedFood.getId());
    }

}
