package com.order.orderlink.region.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.region.domain.Region;

public interface RegionRepository extends JpaRepository<Region, UUID> {
	List<Region> findByParent(Region region);
}
