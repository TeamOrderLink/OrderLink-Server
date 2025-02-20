package com.order.orderlink.deliverydetail.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.deliverydetail.domain.DeliveryDetail;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, UUID> {

	Page<DeliveryDetail> findByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

}
