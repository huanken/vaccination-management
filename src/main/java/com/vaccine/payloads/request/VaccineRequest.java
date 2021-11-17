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
public class VaccineRequest {
	@NotEmpty(message = "Vaccine must be not empty!")
	@Size(min = 5, max = 50, message = "Length of vaccine must be 5 to 50.")
	private String vaccineName;
	
	@NotEmpty(message = "Description must be not empty!")
	@Size(min = 5, max = 255, message = "Length of description must be 5 to 255.")
	private String description;

	@NotNull(message = "Price must be not null!")
	@Min(value = 100, message = "Price must be higher 100.")
	private float price;
	
	@NotNull(message = "Amount must be not null!")
	@Min(value = 1, message = "Amount must be higher 1.")
	private int amount;
	
	@Pattern(regexp = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) (0?[0-9]|1?[0-9]|2?[0-3]):([0-5][0-9])$", message = "Date malformed! (yyyy-MM-dd HH:mm)")
	private String expiryDate;

	@NotEmpty(message = "Manufacture must be not empty!")
	@Size(min = 5, max = 50, message = "Length of manufacture must be 5 to 50.")
	private String manufacture;
}
