package com.order.orderlink.payment.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.auth.UserDetailsServiceImpl;
import com.order.orderlink.common.auth.util.JwtUtil;
import com.order.orderlink.common.client.OrderClient;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.order.domain.OrderStatus;
import com.order.orderlink.order.domain.OrderType;
import com.order.orderlink.payment.application.dtos.PaymentRequest;
import com.order.orderlink.payment.application.dtos.PaymentResponse;
import com.order.orderlink.payment.domain.Payment;
import com.order.orderlink.payment.domain.repository.PaymentRepository;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.UserRoleEnum;

import jakarta.servlet.http.HttpServletRequest;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
public class PaymentServiceTest {
	@MockitoBean
	JwtUtil jwtUtil;

	@Autowired
	PaymentService paymentService;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private HttpServletRequest httpServletRequest;

	@MockitoBean
	OrderClient orderClient;

	UUID orderId = null;
	String accessToken;

	@BeforeEach
	void setUp() {
		Order order = Order.builder()
			.userId(UUID.randomUUID())
			.restaurantId(UUID.randomUUID())
			.deliveryAddress("123 Main St, Seoul")
			.totalPrice(25000)
			.status(OrderStatus.NEW)
			.orderType(OrderType.ONLINE)
			.orderItems(new ArrayList<>())
			.build();

		accessToken = "Bearer mockAccessToken";
		//WebClient로 order 반환
		when(orderClient.getOrder(orderId, accessToken)).thenReturn(order);
		orderId = order.getId();

	}

	@DisplayName("결제내역 생성 성공")
	@Test
	void createPayment() {
		//given
		PaymentRequest.Create request = PaymentRequest.Create.builder()
			.orderId(orderId)
			.cardNumber("110343423421")
			.bank("신한")
			.cardHolder("홍길동")
			.expiryDate("24/7")
			.amount(25000)
			.build();
		User user = new User("홍길동", "mr.hong", "hong@gmail.com", "01011112222",
			"$2a$10$BbKYV3eeMYWBHcgxBLo11uRlugF3In804idrVHSzvqbZRmuvpN8Q6", UserRoleEnum.CUSTOMER);
		UserDetailsImpl userDetails = new UserDetailsImpl(user);
		Authentication authentication =
			new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//when
		PaymentResponse.Create response = paymentService.createPayment(userDetails, request);

		//then
		assertNotNull(response);
		assertNotNull(response.getPaymentId());
		Payment payment = paymentRepository.findById(response.getPaymentId()).orElse(null);
		assertThat(payment.getOrder().getId()).isEqualTo(orderId);
	}
}
