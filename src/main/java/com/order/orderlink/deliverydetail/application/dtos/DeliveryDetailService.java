package com.order.orderlink.deliverydetail.application.dtos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.DeliveryDetailException;
import com.order.orderlink.deliverydetail.domain.DeliveryDetail;
import com.order.orderlink.deliverydetail.domain.repository.DeliveryDetailRepository;
import com.order.orderlink.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryDetailService {

	private final DeliveryDetailRepository deliveryDetailRepository;

	// 배송 상세 생성
	public DeliveryDetailResponse.Create createDeliveryDetail(User user,
		DeliveryDetailRequest.Create request) {
		DeliveryDetail deliveryDetail = DeliveryDetail.builder()
			.user(user)
			.address(request.getAddress())
			.request(request.getRequest())
			.isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
			.build();
		deliveryDetailRepository.save(deliveryDetail);
		return new DeliveryDetailResponse.Create(deliveryDetail.getId());
	}

	// 배송 상세 전체 목록 조회
	@Transactional(readOnly = true)
	public DeliveryDetailResponse.ReadAll getDeliveryDetails(UUID userId, Pageable pageable) {
		Page<DeliveryDetail> page = deliveryDetailRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
		List<DeliveryDetailResponse.Read> deliveryDetails = page.getContent().stream()
			.map(deliveryDetail -> new DeliveryDetailResponse.Read(
				deliveryDetail.getId(),
				deliveryDetail.getAddress(),
				deliveryDetail.getRequest(),
				deliveryDetail.getIsDefault(),
				deliveryDetail.getCreatedAt()
			))
			.toList();
		return new DeliveryDetailResponse.ReadAll(deliveryDetails, page.getNumber() + 1, page.getTotalPages(),
			page.getTotalElements());
	}

	// 배송 상세 단건 조회
	@Transactional(readOnly = true)
	public DeliveryDetailResponse.Read getDeliveryDetail(UUID deliveryDetailId) {
		DeliveryDetail deliveryDetail = getDeliveryDetailInstance(deliveryDetailId);
		return new DeliveryDetailResponse.Read(
			deliveryDetail.getId(),
			deliveryDetail.getAddress(),
			deliveryDetail.getRequest(),
			deliveryDetail.getIsDefault(),
			deliveryDetail.getCreatedAt()
		);
	}

	// 배송 상세 수정
	public DeliveryDetailResponse.Update updateDeliveryDetail(UUID deliveryDetailId,
		DeliveryDetailRequest.Update request) {
		DeliveryDetail deliveryDetail = getDeliveryDetailInstance(deliveryDetailId);
		deliveryDetail.update(request.getAddress(), request.getRequest(), request.getIsDefault());
		return new DeliveryDetailResponse.Update(deliveryDetail.getId(), deliveryDetail.getAddress(),
			deliveryDetail.getRequest(), deliveryDetail.getIsDefault());
	}

	// 배송 상세 삭제 (soft delete)
	public void deleteDeliveryDetail(UUID deliveryDetailId, String username) {
		DeliveryDetail deliveryDetail = getDeliveryDetailInstance(deliveryDetailId);
		deliveryDetail.softDelete(username);
	}

	private DeliveryDetail getDeliveryDetailInstance(UUID deliveryDetailId) {
		return deliveryDetailRepository.findById(deliveryDetailId)
			.orElseThrow(() -> new DeliveryDetailException(ErrorCode.DELIVERY_DETAIL_NOT_FOUND));
	}

}
