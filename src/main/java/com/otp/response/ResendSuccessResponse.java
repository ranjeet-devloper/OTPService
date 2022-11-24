package com.otp.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResendSuccessResponse {
	
	private String successcode;
	private String message;

}
