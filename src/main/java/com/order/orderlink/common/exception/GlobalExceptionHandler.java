package com.order.orderlink.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({TokenNotValidException.class})
	public ResponseEntity<RestApiException> tokenNotValidExceptionHandler(TokenNotValidException ex) {
		RestApiException restApiException = new RestApiException(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
		return new ResponseEntity<>(restApiException, HttpStatus.UNAUTHORIZED);
	}

}
