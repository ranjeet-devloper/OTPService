package com.otp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otp.model.User;
import com.otp.serviceimpl.TOTP;

@RestController
public class OTPController {
	
	@Autowired
	TOTP totp;
	
	@PostMapping("/generateOTP")
	public String generateOTP()
	{
		String sec="3132333435363738393031323334353637383930" +
		         "313233343536373839303132";
	return "otp"+":"+this.totp.generateTOTP256(sec,"30", "6");
//		return "hiii";
		
	}
	
	
	@PostMapping("/validateOTP")
	public ResponseEntity<?> validateOTP()
	{
		return null;
	}

}
