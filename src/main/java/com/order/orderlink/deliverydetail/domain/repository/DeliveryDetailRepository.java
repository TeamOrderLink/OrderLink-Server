package com.order.orderlink.deliverydetail.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.deliverydetail.domain.DeliveryDetail;
import com.order.orderlink.user.domain.User;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, UUID> {

	List<DeliveryDetail> findByUser(User user);

	Optional<DeliveryDetail> findByUserAndIsDefault(User user, Boolean isDefault);
}
