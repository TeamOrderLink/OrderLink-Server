package com.order.orderlink.payment.application;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.client.OrderClient;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AuthException;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.payment.application.dtos.PaymentRequest;
import com.order.orderlink.payment.application.dtos.PaymentResponse;
import com.order.orderlink.payment.domain.Payment;
import com.order.orderlink.payment.domain.PaymentStatus;
import com.order.orderlink.payment.domain.repository.JpaPaymentRepository;
import com.order.orderlink.user.domain.UserRoleEnum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
	private final OrderClient orderClient;
	private final JpaPaymentRepository paymentRepository;

	public PaymentResponse.Create createPayment(UserDetailsImpl userDetails, PaymentRequest.Create request,
		HttpServletRequest httpServletRequest) {
		UUID userId = validateRoleAndGetUserId(userDetails, Arrays.asList(UserRoleEnum.CUSTOMER));
		String accessToken = httpServletRequest.getHeader("Authorization");
		Order order = orderClient.getOrder(request.getOrderId(), accessToken);
		Payment payment = Payment.builder()
			.order(order)
			.amount(request.getAmount())
			.bank(request.getBank())
			.cardHolder(request.getCardHolder())
			.cardNumber(request.getCardNumber())
			.expiryDate(request.getExpiryDate())
			.status(PaymentStatus.COMPLETED)
			.build();
		paymentRepository.save(payment);
		return PaymentResponse.Create.builder().paymentId(payment.getId()).build();
	}

	private static UUID getUserId(UserDetailsImpl userDetails) {
		UUID userId = userDetails.getUser().getId();
		return userId;
	}

	private static UUID validateRoleAndGetUserId(UserDetailsImpl userDetails, List<UserRoleEnum> roles) {
		if (!roles.contains(userDetails.getUser().getRole())) {
			throw new AuthException(ErrorCode.USER_ACCESS_DENIED);
		}
		UUID userId = getUserId(userDetails);
		return userId;
	}
}
