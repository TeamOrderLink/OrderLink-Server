package com.order.orderlink.ai.application;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.orderlink.ai.application.dtos.AiApiRequest;
import com.order.orderlink.ai.application.dtos.AiApiResponse;
import com.order.orderlink.ai.domain.AiApiLog;
import com.order.orderlink.ai.domain.repository.AiApiLogRepository;
import com.order.orderlink.common.enums.ErrorCode;
import com.order.orderlink.common.exception.AiApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AI API Service")
@Service
@RequiredArgsConstructor
public class AiApiService {

	private final WebClient aiWebClient;
	private final AiApiLogRepository aiApiLogRepository;

	@Value("${ai.api.key}")
	private String apiKey;

	public AiApiResponse.GenerateDescription generateDescription(UUID userId,
		AiApiRequest.GenerateDescription requestDto) {

		int MAX_INPUT_LENGTH = 100; // AI API 요청 텍스트 최대 길이

		String baseText = requestDto.getBaseText();

		if (baseText.length() > MAX_INPUT_LENGTH) {
			baseText = baseText.substring(0, MAX_INPUT_LENGTH);
			log.info("요청 텍스트가 {}자를 초과하여 {}자로 자름", MAX_INPUT_LENGTH, baseText.length());
		}

		// 요청 텍스트에 추가 지시문 첨부
		String finalRequestText = baseText + " 답변을 최대한 간결하게 50자 이하로";

		// AI API에 보낼 payload 구성 (요청 JSON 구조)
		Map<String, Object> payload = new HashMap<>();
		Map<String, Object> contentMap = new HashMap<>();
		Map<String, String> part = new HashMap<>();
		part.put("text", finalRequestText);
		contentMap.put("parts", new Map[] {part}); // parts 는 배열 형태로 전달
		payload.put("contents", new Map[] {contentMap});

		log.info("상품 설명 생성 요청 userId: {}, input: {}", userId, baseText);
		String responseBody = aiWebClient.post()
			.uri(uriBuilder -> uriBuilder
				.path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
				.queryParam("key", apiKey)
				.build())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.bodyValue(payload)
			.retrieve()
			.bodyToMono(String.class)
			.block();

		String recommendedDescription = extractDescription(responseBody);
		log.info("상품 설명 생성 응답: {}", recommendedDescription);

		// DB에 API 로그 저장
		AiApiLog aiApiLog = AiApiLog.builder()
			.userId(userId)
			.requestText(baseText)
			.responseText(recommendedDescription)
			.build();
		aiApiLogRepository.save(aiApiLog);

		return new AiApiResponse.GenerateDescription(recommendedDescription);
	}

	private String extractDescription(String responseBody) {
		// 응답 JSON 구조에서 추천 설명 추출
		// 예시: {"contents":[{"parts":[{"text":"상품 설명"}]}]}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(responseBody);
			JsonNode candidates = root.path("candidates");
			if (candidates.isArray() && !candidates.isEmpty()) {
				JsonNode candidate = candidates.get(0);
				JsonNode content = candidate.path("content");
				JsonNode parts = content.path("parts");
				if (parts.isArray() && !parts.isEmpty()) {
					return parts.get(0).path("text").asText();
				}
			}
		} catch (Exception e) {
			log.error("AI API 응답 JSON 파싱 오류", e);
			throw new AiApiException(ErrorCode.AI_API_RESPONSE_PARSING_ERROR);
		}
		return "";
	}

}
