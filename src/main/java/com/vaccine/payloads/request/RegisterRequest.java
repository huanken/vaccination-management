package com.vaccine.payloads.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	@NotEmpty(message = "Username must be not empty!")
	@Size(min = 5, max = 255, message = "Length of username must be 5 to 255.")
	private String username;

	@NotEmpty(message = "Password must be not empty!")
	@Size(min = 5, max = 255, message = "Length of password must be 5 to 255.")
	private String password;
	
	@NotEmpty(message = "Fullname must be not empty!")
	@Size(min = 5, max = 255, message = "Length of fullname must be 5 to 255.")
	private String fullname;
	
	@NotNull(message = "Age must be not null!")
	@Min(value = 12, message = "Age must be higher 12.")
	private Integer age;
	
	@NotEmpty(message = "Address must be not empty!")
	private String address;
	
	@NotEmpty(message = "Phone must be not empty!")
	@Size(min = 10, max = 15, message = "Length of phone must be 10 to 15.")
	private String phone;
	
	@Email(message = "Email invalid!")
	@NotEmpty(message = "Email must be not empty!")
	@Size(min = 5, max = 255, message = "Length of email must be 5 to 255.")
	private String email;
	
	@NotEmpty(message = "Citizen must be not empty!")
	@Size(min = 9, max = 20, message = "Length of citizen must be 9 to 20.")
	private String citizenId;
	
	private Integer prioritize = 0;

	private String[] roles;
}
