package com.order.orderlink.order.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.order.application.OrderService;
import com.order.orderlink.order.application.dtos.OrderRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<?> getOrders() {
		return ResponseEntity.ok("getOrders() has been called!");
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrder(@PathVariable("id") String id) {
		return ResponseEntity.ok("getOrder(id) has been called!");
	}

	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest.Create request) {
		return ResponseEntity.ok(orderService.createOrder(request));
	}

}
