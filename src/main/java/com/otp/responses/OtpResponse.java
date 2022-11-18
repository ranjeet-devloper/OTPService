package com.otp.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtpResponse{

	private String statusCode;
	private String traceID;
	private String otp;
	private String message;
	private String id;
	
}
