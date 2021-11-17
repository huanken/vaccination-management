package com.vaccine.payloads.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	@NotEmpty(message = "Username must be not empty!")
	@Size(min = 5, max = 255, message = "Length of username must be 5 to 255.")
	private String username;

	@NotEmpty(message = "Password must be not empty!")
	@Size(min = 5, max = 255, message = "Length of password must be 5 to 255.")
	private String password;
}
