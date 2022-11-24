package com.otp.controllers;

import java.time.Instant;
import java.time.LocalTime;

import javax.validation.Valid;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otp.dto.OtpDto;
import com.otp.dto.UserDto;
import com.otp.exception.OtpException;
import com.otp.response.OtpResponse;
import com.otp.response.OtpValidationSuccessResponse;
import com.otp.response.ResendSuccessResponse;
import com.otp.serviceimpl.TOTP;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1.0.0/otp")
public class OTPController {

	@Autowired
	TOTP totp;
	
	
	private Long ids=0l;

	@PostMapping("/generateOTP")
	public ResponseEntity<OtpResponse> generateOTP(@Valid @RequestBody UserDto userdto) {

		try {
			String otp = this.totp.generateTOTP256(userdto);
			OtpResponse res = OtpResponse.builder().id(ids++).traceID(Instant.now().toEpochMilli())
					.message("otp generated successfully").otp(otp).statusCode("200").build();
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception ex) {
			throw new OtpException("otp not generated");
		}

	}

	@PostMapping("/validateOTPs")
	public ResponseEntity<?> validateOTP(@RequestBody OtpDto otpdto) {
		return this.totp.validateOtp("" + otpdto.getOtp());

	}

	@PostMapping("/resendOTP")
	public ResponseEntity<?> resendOtp() {

		String msg = this.totp.resendOtp();
		if (msg.length() == 6) {
			ResendSuccessResponse res = ResendSuccessResponse.builder().successcode("200")
					.message("OTP resend successfully").build();
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(msg, HttpStatus.OK);
		}
	}

}
