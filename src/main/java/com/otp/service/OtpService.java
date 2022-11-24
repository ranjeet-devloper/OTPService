package com.otp.service;

import org.springframework.http.ResponseEntity;

import com.otp.dto.OtpDto;
import com.otp.dto.UserDto;

public interface OtpService {
	
	public String generateTOTP256(UserDto userdto);
	public String generateTOTP(String key, String time, String returnDigits,String crypto);
	
	public byte[] hmacSHA(String crypto, byte[] keyBytes,byte[] text);
	public byte[] hexStr2Bytes(String hex);
	public ResponseEntity<?> validateOtp(String otp);
	public String resendOtp();
}
