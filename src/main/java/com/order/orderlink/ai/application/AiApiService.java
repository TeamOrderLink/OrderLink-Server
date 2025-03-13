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
import com.order.orderlink.ai.exception.AiApiException;
import com.order.orderlink.common.enums.ErrorCode;

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

	private static final int MAX_INPUT_LENGTH = 100; // AI API 요청 텍스트 최대 길이
	private static final String ADDITIONAL_INSTRUCTION = " 답변을 최대한 간결하게 50자 이하로";

	public AiApiResponse.GenerateDescription generateDescription(UUID userId,
		AiApiRequest.GenerateDescription requestDto) {

		String inputText = truncateText(requestDto.getBaseText());
		String formattedRequestText = inputText + ADDITIONAL_INSTRUCTION;

		Map<String, Object> payload = createPayload(formattedRequestText);

		log.info("상품 설명 생성 요청 userId: {}, input: {}", userId, inputText);

		String responseBody = sendRequest(payload);
		String recommendedDescription = parseDescriptionFromResponse(responseBody);

		log.info("상품 설명 생성 응답: {}", recommendedDescription);

		saveApiLog(userId, inputText, recommendedDescription);

		return AiApiResponse.GenerateDescription.builder()
			.description(recommendedDescription)
			.build();
	}

	private String truncateText(String text) {
		if (text.length() > MAX_INPUT_LENGTH) {
			String truncatedText = text.substring(0, MAX_INPUT_LENGTH);
			log.info("요청 텍스트가 {}자를 초과하여 {}자로 자름", MAX_INPUT_LENGTH, truncatedText.length());
			return truncatedText;
		}
		return text;
	}

	private Map<String, Object> createPayload(String requestText) {
		Map<String, Object> payload = new HashMap<>();
		Map<String, Object> contentMap = new HashMap<>();
		Map<String, String> part = new HashMap<>();
		part.put("text", requestText);
		contentMap.put("parts", new Map[] {part});
		payload.put("contents", new Map[] {contentMap});
		return payload;
	}

	private String sendRequest(Map<String, Object> payload) {
		return aiWebClient.post()
			.uri(uriBuilder -> uriBuilder
				.path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
				.queryParam("key", apiKey)
				.build())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.bodyValue(payload)
			.retrieve()
			.bodyToMono(String.class)
			.block();
	}

	private String parseDescriptionFromResponse(String responseBody) {
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

	private void saveApiLog(UUID userId, String requestText, String responseText) {
		AiApiLog aiApiLog = AiApiLog.builder()
			.userId(userId)
			.requestText(requestText)
			.responseText(responseText)
			.build();
		aiApiLogRepository.save(aiApiLog);
	}

}
