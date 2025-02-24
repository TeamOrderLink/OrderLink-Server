package com.order.orderlink.payment.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
