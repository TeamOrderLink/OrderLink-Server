package com.order.orderlink.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.auth.util.JwtUtil;
import com.order.orderlink.order.domain.repository.OrderRepository;
import com.order.orderlink.orderitem.domain.OrderItem;
import com.order.orderlink.restaurant.domain.Restaurant;
import com.order.orderlink.restaurant.domain.repository.RestaurantRepository;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
public class OrderRepositoryTest {
	@MockitoBean
	JwtUtil jwtUtil;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	@BeforeEach
	void setUp() {
		Restaurant restaurant = Restaurant.builder()
			.name("꼰미고")
			.address("서울시 강남구 ")
			.phone("01011112222")
			.description("마싯는 타코집")
			.openTime(LocalTime.now())
			.closeTime(LocalTime.now())
			.ownerName("경린")
			.businessRegNum("001020120")
			.build();
		restaurantRepository.save(restaurant);

		List<OrderItem> items = new ArrayList<>();
		Order order = Order.builder()
			.userId(UUID.randomUUID())
			.restaurantId(restaurant.getId())
			.deliveryAddress("123 Main St, Seoul")
			.totalPrice(25000)
			.status(OrderStatus.NEW)
			.orderType(OrderType.ONLINE)
			.orderItems(items)
			.build();
		order.addOrderItem(
			OrderItem.builder()
				.order(order)
				.foodName("타코")
				.price(14000)
				.quantity(2)
				.build()
		);
		orderRepository.save(order);

	}

	@DisplayName("Status 로 order 검색 성공")
	@Test
	void findOrderByStatusSuccess() {
		//given
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		//when
		Page<Order> orders = orderRepository.searchOrdersWithItems(OrderStatus.NEW, null, null, null, null, pageable);

		//then
		assertThat(orders).isNotEmpty();
		assertThat(orders.getContent().get(0).getStatus()).isEqualTo(OrderStatus.NEW);
	}

	@DisplayName("restaurantName 로 order 검색 성공")
	@Test
	void findOrderByRestaurantNameSuccess() {
		//given
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		//when
		Page<Order> orders = orderRepository.searchOrdersWithItems(null, "꼰미고", null, null, null, pageable);

		//then
		assertThat(orders).isNotEmpty();
		UUID restaurantId = orders.getContent().get(0).getRestaurantId();
		Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
		assertThat(restaurant).isNotNull();
		assertThat(restaurant.getName()).isEqualTo("꼰미고");
	}

	@DisplayName("foodName 로 order 검색 성공")
	@Test
	void findOrderByFoodNameSuccess() {
		//given
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		//when
		Page<Order> orders = orderRepository.searchOrdersWithItems(null, null, "타코", null, null, pageable);

		//then
		assertThat(orders).isNotEmpty();
		assertThat(orders.getContent().get(0).getOrderItems().get(0).getFoodName()).isEqualTo("타코");
	}

	@DisplayName("기간으로 order 검색 성공")
	@Test
	void findOrderByDatesSuccess() {
		//given
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		//when
		Page<Order> orders = orderRepository.searchOrdersWithItems(null, null, null, LocalDate.now(),
			LocalDate.now().plusDays(7), pageable);

		//then
		assertThat(orders).isNotEmpty();
		assertThat(orders.getContent().get(0).getCreatedAt().toLocalDate()).isEqualTo(LocalDate.now());
	}

	@DisplayName("다수 조건 중 하나라도 실패하면 실패")
	@Test
	void findIfOneFail() {
		//given
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		//when
		Page<Order> orders = orderRepository.searchOrdersWithItems(OrderStatus.NEW, null, null,
			LocalDate.now().plusDays(6),
			LocalDate.now().plusDays(7), pageable);

		//then
		assertThat(orders).isEmpty();
	}

}
