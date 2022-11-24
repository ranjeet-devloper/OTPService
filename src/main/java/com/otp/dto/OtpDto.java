package com.otp.dto;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
@ToString
@AllArgsConstructor
@Getter
public class OtpDto {
	
	private String name;
	private Integer otp;

}
