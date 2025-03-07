package com.order.orderlink.region.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.region.exception.RegionException;
import com.order.orderlink.region.application.dtos.RegionDTO;
import com.order.orderlink.region.application.dtos.RegionRequest;
import com.order.orderlink.region.application.dtos.RegionResponse;
import com.order.orderlink.region.domain.Region;
import com.order.orderlink.region.domain.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionService {
	private final RegionRepository regionRepository;

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
		Region region = getRegion(regionId);

		if (request.getRegionName() != null && !request.getRegionName().isBlank()) {
			region.updateName(request.getRegionName());
		}

		UUID newParentId = request.getParentId();

		// null이면 최상위
		if (newParentId != null) {
			// 자신을 부모로 설정하는지 확인
			if (region.getId().equals(newParentId)) {
				throw new RegionException(ErrorCode.REGION_NOT_PARENT);
			}

			// 새로운 부모 지역 조회
			Region newParent = getRegion(newParentId);

			region.updateParent(newParent);
		} else {
			// 부모 ID가 null이면 최상위 지역으로 설정
			region.updateParent(null);
		}
	}

	private Region getRegion(UUID regionId) {
		Region region = regionRepository.findById(regionId)
			.orElseThrow(() -> new RegionException(ErrorCode.REGION_NOT_FOUND));
		return region;
	}

	public void deleteRegion(UserDetailsImpl userDetails, UUID regionId) {
		Region region = getRegion(regionId);
		// 부모의 부모 정보 가져오기
		Region grandParent = region.getParent();

		// 하위 지역들의 부모를 Grand Parent로 변경
		List<Region> childRegions = regionRepository.findByParent(region);
		for (Region child : childRegions) {
			child.updateParent(grandParent);
		}
		region.softDelete(userDetails.getUser().getUsername());
	}

	@Transactional(readOnly = true)
	public List<RegionResponse.GetTreeRegions> getRegionTree() {
		List<Region> regions = regionRepository.findAll();
		Map<UUID, List<Region>> childMap = new HashMap<>();

		// 모든 지역을 parentId 기준으로 그룹화
		for (Region region : regions) {
			UUID parentId = region.getParent() != null ? region.getParent().getId() : null;
			childMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(region);
		}

		return buildTree(null, childMap);
	}

	private List<RegionResponse.GetTreeRegions> buildTree(UUID parentId, Map<UUID, List<Region>> childMap) {
		return childMap.getOrDefault(parentId, Collections.emptyList()).stream()
			.map(region -> new RegionResponse.GetTreeRegions(
				region.getId(),
				region.getName(),
				parentId,
				buildTree(region.getId(), childMap)
			))
			.collect(Collectors.toList());
	}
}
