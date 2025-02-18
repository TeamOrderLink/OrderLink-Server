package com.order.orderlink.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(500, "서버 내부 오류가 발생했습니다."));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getField() + " - " + error.getDefaultMessage())
			.collect(Collectors.joining("; "));

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
	}

	@Getter
	@AllArgsConstructor
	public static class ErrorResponse {
		private int status;
		private String message;
	}

}
