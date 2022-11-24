package com.otp.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OtpValidationSuccessResponse {

	
	private long id;
	private long statuscode;
	private long traceid;
	private String message;
	
	
	/*
	 * "StatusCode": 200, "TraceID":"79847546578789675" "Message":
	 * "OTP validate successfully", "ID":1
	 */

	
}
