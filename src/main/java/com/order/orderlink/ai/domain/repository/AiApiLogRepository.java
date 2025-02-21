package com.order.orderlink.ai.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.ai.domain.AiApiLog;

public interface AiApiLogRepository extends JpaRepository<AiApiLog, UUID> {
}
