package com.order.orderlink.ai.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AiApiResponse {

	@Getter
	@AllArgsConstructor
	public static class GenerateDescription {
		private String description;
	}

}
