package com.vaccine.payloads.response;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponse {
	private int status;
	private String error;
	private String message;
	@JsonInclude(value = Include.NON_NULL)
	private Map<String, ?> details;

	public ErrorResponse(HttpStatus httpStatus, String message, Map<String, ?> details) {
		this.status = httpStatus.value();
		this.error = httpStatus.name();
		this.message = message;
		this.details = details;
	}
}
