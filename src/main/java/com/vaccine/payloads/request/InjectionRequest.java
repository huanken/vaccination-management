package com.vaccine.payloads.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InjectionRequest {
	@NotEmpty(message = "Vaccine name must be not empty!")
	@Size(min = 5, max = 255, message = "Length of vaccine name must be 5 to 255.")
	private String vaccineName;

	@NotEmpty(message = "Location must be not empty!")
	@Size(min = 5, max = 255, message = "Length of location must be 5 to 255.")
	private String locationName;

	@Pattern(regexp = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) (0?[0-9]|1?[0-9]|2?[0-3]):([0-5][0-9])$", message = "Date injection malformed! (yyyy-MM-dd HH:mm)")
	private String dateInjection;
	
	private String status = "Đang chờ";
}
