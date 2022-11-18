package com.otp.service;

public interface OtpService {
	
	public String generateTOTP256(String key,String time, String returnDigits);
	public String generateTOTP(String key, String time, String returnDigits,String crypto);
	
	public byte[] hmacSHA(String crypto, byte[] keyBytes,byte[] text);
	public byte[] hexStr2Bytes(String hex);
}
