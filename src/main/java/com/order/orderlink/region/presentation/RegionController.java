package com.order.orderlink.region.presentation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.common.dtos.SuccessNonDataResponse;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.region.application.RegionService;
import com.order.orderlink.region.application.dtos.RegionRequest;
import com.order.orderlink.region.application.dtos.RegionResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {
	private final RegionService regionService;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<RegionResponse.Create> createRegion(
		@Valid @RequestBody RegionRequest.Create request
	) {
		return SuccessResponse.success(SuccessCode.REGION_CREATE_SUCCESS, regionService.createRegion(request));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessResponse<RegionResponse.GetRegions> getRegions(
	) {
		return SuccessResponse.success(SuccessCode.REGION_GET_SUCCESS, regionService.getRegions());
	}

	@PatchMapping("/{regionId}")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessNonDataResponse updateRegions(
		@PathVariable UUID regionId,
		@RequestBody RegionRequest.Update request
	) {
		regionService.updateRegion(regionId, request);
		return SuccessNonDataResponse.success(SuccessCode.REGION_UPDATE_SUCCESS);
	}

	@DeleteMapping("/{regionId}")
	@PreAuthorize("hasAuthority('ROLE_MASTER')")
	public SuccessNonDataResponse deleteRegions(
		@PathVariable UUID regionId
	) {
		regionService.deleteRegion(regionId);
		return SuccessNonDataResponse.success(SuccessCode.REGION_DELETE_SUCCESS);
	}
}
