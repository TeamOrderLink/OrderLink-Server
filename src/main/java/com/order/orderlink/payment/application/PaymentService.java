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
import com.order.orderlink.payment.exception.PaymentException;
import com.order.orderlink.order.domain.Order;
import com.order.orderlink.payment.application.dtos.PaymentRequest;
import com.order.orderlink.payment.application.dtos.PaymentResponse;
import com.order.orderlink.payment.domain.Payment;
import com.order.orderlink.payment.domain.PaymentStatus;
import com.order.orderlink.payment.domain.repository.PaymentRepository;
import com.order.orderlink.user.domain.UserRoleEnum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
	private final OrderClient orderClient;
	private final PaymentRepository paymentRepository;

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

	@Transactional(readOnly = true)
	public PaymentResponse.GetPayment getPayment(UserDetailsImpl userDetails, UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new PaymentException(ErrorCode.PAYMENT_NOT_FOUND));
		return PaymentResponse.GetPayment.builder()
			.amount(payment.getAmount())
			.bank(payment.getBank())
			.cardHolder(payment.getCardHolder())
			.cardNumber(maskCardNumber(payment.getCardNumber()))
			.createdAt(payment.getCreatedAt())
			.status(payment.getStatus().getValue())
			.build();
	}

	private String maskCardNumber(String cardNumber) {
		if (cardNumber != null && cardNumber.length() > 4) {
			// 카드 번호의 마지막 4자리 제외하고 '*'로 마스킹
			String last4Digits = cardNumber.substring(cardNumber.length() - 4);
			String masked = cardNumber.substring(0, cardNumber.length() - 4).replaceAll("[0-9]", "*");
			return masked + last4Digits;
		}
		return cardNumber;
	}
}
