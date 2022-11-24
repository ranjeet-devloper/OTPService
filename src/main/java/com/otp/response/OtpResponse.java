package com.otp.response;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OtpResponse{

	private String statusCode;
	private Long traceID;
	private String otp;
	private String message;
	private long id;
	
}
