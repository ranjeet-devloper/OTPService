package com.otp.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class ErrorResponse {

	
	private int errorcode;
	private LocalDateTime timestamp;
	private String statuscode;
	private String error;
	private long traceid;
	private Object trace;
	private String path;
	
	
	
}
