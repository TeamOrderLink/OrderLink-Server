package com.order.orderlink.address.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.address.domain.Address;

public interface AddressRepository extends JpaRepository<Address, UUID> {

	Page<Address> findByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

}
