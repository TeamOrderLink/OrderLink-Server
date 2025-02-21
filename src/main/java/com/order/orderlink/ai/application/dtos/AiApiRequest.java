package com.order.orderlink.ai.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class AiApiRequest {

	@Getter
	public static class GenerateDescription {
		@NotBlank(message = "입력 텍스트는 필수입니다.")
		@Size(max = 100, message = "입력 텍스트는 최대 100자까지 입력 가능합니다.")
		private String baseText;
	}

}
