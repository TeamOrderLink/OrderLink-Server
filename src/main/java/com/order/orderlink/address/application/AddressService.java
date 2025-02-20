package com.order.orderlink.address.application;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.orderlink.address.application.dtos.AddressRequest;
import com.order.orderlink.address.application.dtos.AddressResponse;
import com.order.orderlink.address.domain.Address;
import com.order.orderlink.address.domain.repository.AddressRepository;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AddressException;
import com.order.orderlink.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

	private final AddressRepository addressRepository;

	// 배송지 등록
	public AddressResponse.Create createAddress(User user,
		AddressRequest.Create request) {

		// 기존에 설정된 기본 배송지가 있으면 기본 배송지 해제
		if (Boolean.TRUE.equals(request.getIsDefault())) {
			List<Address> currentDefaultAddresses = addressRepository.findByUserIdAndIsDefaultTrue(user.getId());
			currentDefaultAddresses.forEach(address -> address.update(null, false));
		}

		Address address = Address.builder()
			.user(user)
			.address(request.getAddress())
			.isDefault(Boolean.TRUE.equals(request.getIsDefault()))
			.build();
		addressRepository.save(address);
		return new AddressResponse.Create(address.getId());
	}

	// 배송지 목록 조회
	@Transactional(readOnly = true)
	public AddressResponse.ReadAll getAddresses(UUID userId, Pageable pageable) {
		Page<Address> page = addressRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
		List<AddressResponse.Read> addresses = page.getContent().stream()
			.map(address -> new AddressResponse.Read(
				address.getId(),
				address.getAddress(),
				address.getIsDefault(),
				address.getCreatedAt()
			))
			.toList();
		return new AddressResponse.ReadAll(addresses, page.getNumber() + 1, page.getTotalPages(),
			page.getTotalElements());
	}

	// 배송지 상세 조회
	@Transactional(readOnly = true)
	public AddressResponse.Read getAddressInfo(UUID addressId, UUID currentUserId) {
		Address address = getAddressInstance(addressId, currentUserId);
		return new AddressResponse.Read(
			address.getId(),
			address.getAddress(),
			address.getIsDefault(),
			address.getCreatedAt()
		);
	}

	// 배송지 수정
	public AddressResponse.Update updateAddress(UUID addressId, UUID currentUserId, AddressRequest.Update request) {
		Address address = getAddressInstance(addressId, currentUserId);

		// 기존에 설정된 기본 배송지가 있으면 기본 배송지 해제
		if (request.getIsDefault() != null && request.getIsDefault()) {
			List<Address> currentDefaultAddresses = addressRepository.findByUserIdAndIsDefaultTrue(currentUserId);
			currentDefaultAddresses.forEach(addr -> {
				if (!addr.getId().equals(addressId)) {
					addr.update(null, false);
				}
			});
		}

		address.update(request.getAddress(), request.getIsDefault());
		return new AddressResponse.Update(address.getId(), address.getAddress(), address.getIsDefault());
	}

	// 배송지 삭제 (soft delete)
	public void deleteAddress(UUID addressId, UUID currentUserId, String username) {
		Address address = getAddressInstance(addressId, currentUserId);
		address.softDelete(username);
	}

	//
	private Address getAddressInstance(UUID addressId, UUID currentUserId) {
		return addressRepository.findByIdAndUserId(addressId, currentUserId)
			.orElseThrow(() -> new AddressException(ErrorCode.ADDRESS_NOT_FOUND));
	}

}
