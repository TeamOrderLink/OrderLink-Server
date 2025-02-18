package com.order.orderlink.order.presentation;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessNonDataResponse;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.order.application.OrderService;
import com.order.orderlink.order.application.dtos.OrderRequest;
import com.order.orderlink.order.application.dtos.OrderResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping("/my")
	public SuccessResponse<OrderResponse.GetOrders> getMyOrders(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return SuccessResponse.success(SuccessCode.ORDER_GET_SUCCESS,
			orderService.getMyOrders(userDetails, page, size));
	}

	@GetMapping("/{orderId}")
	public SuccessResponse<OrderResponse.GetOrderDetail> getOrderDetail(
		@PathVariable("orderId") UUID orderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return SuccessResponse.success(SuccessCode.ORDER_GET_DETAIL_SUCCESS,
			orderService.getOrderDetail(userDetails, orderId));
	}

	@PostMapping
	public SuccessResponse<OrderResponse.Create> createOrder(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody OrderRequest.Create request) {
		return SuccessResponse.success(SuccessCode.ORDER_CREATE_SUCCESS,
			orderService.createOrder(userDetails, request));
	}

	@PatchMapping("/{orderId}/status")
	public SuccessNonDataResponse updateOrderStatus(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable("orderId") UUID orderId,
		@RequestBody OrderRequest.UpdateStatus request) {
		orderService.updateOrderStatus(userDetails, orderId, request);
		return SuccessNonDataResponse.success(SuccessCode.ORDER_UPDATE_STATUS_SUCCESS);
	}

	@GetMapping
	public SuccessResponse<OrderResponse.GetRestaurantOrders> getRestaurantOrders(
		@RequestParam UUID restaurantId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		HttpServletRequest httpServletRequest
	) {
		return SuccessResponse.success(SuccessCode.ORDERS_OWNER_GET_SUCCESS,
			orderService.getRestaurantOrders(userDetails, restaurantId, page, size, httpServletRequest));
	}

	@GetMapping("/search")
	public SuccessResponse<OrderResponse.GetOrders> getSearchedOrders(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody OrderRequest.Search request
	) {
		return SuccessResponse.success(SuccessCode.ORDERS_SEARCH_SUCCESS,
			orderService.getSearchedOrders(userDetails, request, page, size));
	}

}
