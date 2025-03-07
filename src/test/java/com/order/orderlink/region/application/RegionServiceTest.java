package com.order.orderlink.region.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.auth.util.JwtUtil;
import com.order.orderlink.region.exception.RegionException;
import com.order.orderlink.region.application.dtos.RegionRequest;
import com.order.orderlink.region.domain.Region;
import com.order.orderlink.region.domain.repository.RegionRepository;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
public class RegionServiceTest {
	@MockitoBean
	JwtUtil jwtUtil;

	@Autowired
	RegionService regionService;

	@Autowired
	RegionRepository regionRepository;

	UUID regionId = null;
	UUID parentId = null;

	@BeforeEach
	void setUp() {
		Region parent = Region.builder().name("서울").parent(null).build();
		Region region = Region.builder().name("강남").parent(parent).build();
		regionRepository.save(parent);
		regionRepository.save(region);
		parentId = parent.getId();
		regionId = region.getId();
	}

	@DisplayName("지역 이름 변경 및 최상위 지역으로 변경 성공")
	@Test
	void updateRegionNameSuccess() {
		//given
		RegionRequest.Update request = RegionRequest.Update.builder()
			.regionName("역삼")
			.parentId(null)
			.build();

		//when
		regionService.updateRegion(regionId, request);

		//then
		Region region = regionRepository.findById(regionId).get();
		assertThat(region.getName()).isEqualTo("역삼");
		assertThat(region.getParent()).isNull();

	}

	@DisplayName("parentId가 자기 자신일 때 변경 실패")
	@Test
	void updateMeParentFail() {
		// given
		RegionRequest.Update request = RegionRequest.Update.builder()
			.regionName("강남")
			.parentId(regionId) // 자기 자신을 부모로 설정
			.build();

		// when & then
		assertThrows(RegionException.class, () -> regionService.updateRegion(regionId, request));
	}
}
