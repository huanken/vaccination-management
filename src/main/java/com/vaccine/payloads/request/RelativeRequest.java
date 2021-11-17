package com.vaccine.payloads.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelativeRequest {
	@NotEmpty(message = "Fullname must be not empty!")
	@Size(min = 5, max = 255, message = "Length of fullname must be 5 to 255.")
	private String fullname;

	@NotNull(message = "Age must be not null!")
	@Min(value = 12, message = "Age must be higher 12.")
	private Integer age;

	@NotEmpty(message = "Address must be not empty!")
	private String address;

	@NotEmpty(message = "Citizen must be not empty!")
	@Size(min = 9, max = 20, message = "Length of citizen must be 9 to 20.")
	private String citizenId;
	
	@Pattern(regexp = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) (0?[0-9]|1?[0-9]|2?[0-3]):([0-5][0-9])$", message = "Date injection malformed! (yyyy-MM-dd HH:mm)")
	private String dateInjection;

	private Integer prioritize = 0;
}
