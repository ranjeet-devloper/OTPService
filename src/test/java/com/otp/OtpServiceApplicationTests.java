package com.otp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.otp.dto.UserDto;
import com.otp.response.OtpValidationSuccessResponse;
import com.otp.serviceimpl.TOTP;

@SpringBootTest
class OtpServiceApplicationTests {

	@Autowired
	private TOTP totp;
	
	UserDto userdto=UserDto.builder().email("ranjeet@gmail.com")
			.phone("7903703082").username("ranjeet")
			.build();
	
	@Test
	void otpGenerateTest() {
	   String otp=totp.generateTOTP256(userdto);  
	   assertEquals(6, otp.length());	
	}
		@Test
		void otpValidateTest()
		{
			ResponseEntity<?> res=totp.validateOtp(totp.generateTOTP256(userdto));
			assertEquals(HttpStatus.ACCEPTED,res.getStatusCode());
		}
		

	}

