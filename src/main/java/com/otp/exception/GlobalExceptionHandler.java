package com.otp.exception;

import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.otp.response.ErrorResponse;

import lombok.experimental.StandardException;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
	private int errorcod=1;
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> handleMetodArgumentNotValidException(MethodArgumentNotValidException ex)
	{
		Map<String,String> rep=new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName=((FieldError)error).getField();
			String message=error.getDefaultMessage();
			rep.put(fieldName, message);
		});
		return new ResponseEntity<>(rep, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(OtpException.class)
	public ResponseEntity<ErrorResponse> handleOtpException(Exception ex)
	{
		ErrorResponse obj=ErrorResponse.builder().statuscode("422").errorcode(errorcod++)
				.timestamp(LocalDateTime.now()).error("internal server error")
		.traceid(Instant.now().toEpochMilli()).trace(ex.getMessage()).path("").build();
		return new ResponseEntity<>(obj,HttpStatus.BAD_REQUEST);
		
	}
	
}
