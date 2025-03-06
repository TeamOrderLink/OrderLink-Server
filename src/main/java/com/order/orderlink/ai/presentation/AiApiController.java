package com.order.orderlink.ai.presentation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.orderlink.ai.application.AiApiService;
import com.order.orderlink.ai.application.dtos.AiApiRequest;
import com.order.orderlink.ai.application.dtos.AiApiResponse;
import com.order.orderlink.common.auth.UserDetailsImpl;
import com.order.orderlink.common.dtos.SuccessResponse;
import com.order.orderlink.common.enums.SuccessCode;
import com.order.orderlink.ai.exception.AiApiException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiApiController {

	private final AiApiService aiApiService;

	/**
	 * 음식 설명 생성 요청
	 * @param userDetails 로그인한 사용자 정보
	 * @param request AiApiRequest.GenerateDescription - 요청 DTO
	 * @return SuccessResponse<AiApiResponse.GenerateDescription> - 응답 DTO
	 * @throws AiApiException AI API 응답 파싱 실패시
	 * @see AiApiRequest.GenerateDescription
	 * @see AiApiResponse.GenerateDescription
	 * @see AiApiService#generateDescription(UUID, AiApiRequest.GenerateDescription)
	 * @author Jihwan
	 */
	@PostMapping("/generate-description")
	@PreAuthorize("hasAuthority('ROLE_OWNER')")
	public SuccessResponse<AiApiResponse.GenerateDescription> generateDescription(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody AiApiRequest.GenerateDescription request) {
		UUID userId = userDetails.getUser().getId();
		return SuccessResponse.success(SuccessCode.AI_DESCRIPTION_GENERATE_SUCCESS,
			aiApiService.generateDescription(userId, request));
	}

}
