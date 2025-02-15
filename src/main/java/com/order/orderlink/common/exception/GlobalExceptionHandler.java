package com.order.orderlink.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class) // 모든 커스텀 예외 처리
	public ResponseEntity<ErrorResponse> baseExceptionHandler(BaseException ex) {
		return ResponseEntity
			.status(ex.getStatus())
			.body(new ErrorResponse(ex.getStatus().value(), ex.getMessage()));
	}

	@ExceptionHandler(Exception.class) //일반적인 예외 처리
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return ResponseEntity
			.status(500)
			.body(new ErrorResponse(500, "서버 내부 오류가 발생했습니다."));
	}

	@Getter
	@AllArgsConstructor
	public static class ErrorResponse {
		private int status;
		private String message;
	}

}
