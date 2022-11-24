package com.otp.dto;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
	@NotEmpty
	@NotNull
	@Size(min=2,max=16, message="username length should be 2 to 16")
	private String username;
	
	@Email
	private String email;
	@NotBlank
	@Size(min=10,max=10, message = "phone number should be valid")
	private String phone;
}
