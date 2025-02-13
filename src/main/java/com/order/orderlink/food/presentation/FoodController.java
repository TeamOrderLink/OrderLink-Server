package com.order.orderlink.food.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.food.application.dtos.FoodCreateRequest;

@RestController
@RequestMapping("/foods")
public class FoodController {

	@GetMapping
	public ResponseEntity<?> getProducts() {
		return ResponseEntity.ok("getProducts() has been called!!");
	}

	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody FoodCreateRequest request) {
		System.out.println("request.value() = " + request.value());
		return ResponseEntity.ok(request.value());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProduct(@PathVariable("id") String id) {
		return ResponseEntity.ok("getProduct(%s) has been called!!".formatted(id));
	}

}
