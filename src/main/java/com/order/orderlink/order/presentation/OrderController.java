package com.order.orderlink.order.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.order.application.OrderService;
import com.order.orderlink.order.application.dtos.OrderRequest;
import com.order.orderlink.order.application.dtos.OrderResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping("/my")
	public SuccessResponse<OrderResponse.GetOrders> getMyOrders(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestParam int page,
		@RequestParam int size
	) {
		return SuccessResponse.success(SuccessCode.ORDER_GET_SUCCESS,
			orderService.getMyOrders(userDetails, page, size));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrder(@PathVariable("id") String id) {
		return ResponseEntity.ok("getOrder(id) has been called!");
	}

	@PostMapping
	public SuccessResponse<OrderResponse.Create> createOrder(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody OrderRequest.Create request) {
		return SuccessResponse.success(SuccessCode.ORDER_CREATE_SUCCESS,
			orderService.createOrder(userDetails, request));
	}
}
