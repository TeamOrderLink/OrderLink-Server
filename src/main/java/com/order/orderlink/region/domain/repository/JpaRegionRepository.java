package com.order.orderlink.region.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.region.domain.Region;

public interface JpaRegionRepository extends JpaRepository<Region, UUID> {
}
