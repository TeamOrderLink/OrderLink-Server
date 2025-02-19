package com.order.orderlink.payment.presentation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.payment.application.PaymentService;
import com.order.orderlink.payment.application.dtos.PaymentRequest;
import com.order.orderlink.payment.application.dtos.PaymentResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	public SuccessResponse<PaymentResponse.Create> createPayment(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody PaymentRequest.Create request,
		HttpServletRequest httpServletRequest) {
		return SuccessResponse.success(SuccessCode.PAYMENT_CREATE_SUCCESS,
			paymentService.createPayment(userDetails, request, httpServletRequest));
	}
}
