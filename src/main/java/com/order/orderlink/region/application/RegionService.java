package com.order.orderlink.region.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.RegionException;
import com.order.orderlink.region.application.dtos.RegionDTO;
import com.order.orderlink.region.application.dtos.RegionRequest;
import com.order.orderlink.region.application.dtos.RegionResponse;
import com.order.orderlink.region.domain.Region;
import com.order.orderlink.region.domain.repository.JpaRegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionService {
	private final JpaRegionRepository regionRepository;

	public RegionResponse.Create createRegion(RegionRequest.Create request) {
		UUID parentId = request.getParentId();
		Region parent = null;
		if (parentId != null) {
			parent = regionRepository.findById(parentId)
				.orElseThrow(() -> new RegionException(ErrorCode.REGION_NOT_FOUND));
		}
		Region region = Region.builder().name(request.getRegionName()).parent(parent).build();
		regionRepository.save(region);
		return RegionResponse.Create.builder().regionId(region.getId()).build();
	}

	@Transactional(readOnly = true)
	public RegionResponse.GetRegions getRegions() {
		List<Region> regions = regionRepository.findAll();
		List<RegionDTO> regionDTOs = regions.stream().map(region -> RegionDTO.builder()
			.name(region.getName())
			.regionId(region.getId())
			.createdAt(region.getCreatedAt())
			.build()).toList();
		return RegionResponse.GetRegions.builder().regions(regionDTOs).build();
	}

	public void updateRegion(UUID regionId, RegionRequest.Update request) {
		Region region = regionRepository.findById(regionId)
			.orElseThrow(() -> new RegionException(ErrorCode.REGION_NOT_FOUND));

		String newName = request.getRegionName();
		Region parent = region.getParent();
		UUID newParentId = parent.getId();
		if (newName != null && !newName.isBlank()) {
			region.updateName(newName);
		}

		if (newParentId != null) {
			// 같은 지역을 부모로 설정하는 것을 방지
			if (region.getId().equals(newParentId)) {
				throw new RegionException(ErrorCode.REGION_NOT_PARENT);
			}

			// 새로운 부모 지역 찾기
			Region newParent = regionRepository.findById(newParentId)
				.orElseThrow(() -> new RegionException(ErrorCode.REGION_NOT_FOUND));

			region.updateParent(newParent);
		} else {
			// 부모 ID가 null이면 최상위 지역으로 변경
			region.updateParent(null);
		}
	}

	public void deleteRegion(UUID regionId) {
		regionRepository.deleteById(regionId);
	}
}
