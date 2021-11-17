package com.vaccine.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vaccine.entity.Injection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InjectionDTO {
	private int id;
	private AccountDTO user;
	private VaccineDTO vaccine;
	private String location;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date dateInjection;
	private String status;

	public InjectionDTO(Injection injection) {
		this.id = injection.getId();
		this.user = AccountDTO.build(injection.getUser());
		this.vaccine = VaccineDTO.build(injection.getVaccine());
		this.location = injection.getLocation().getName();
		this.dateInjection = injection.getDateInjection();
		this.status = injection.getStatus();
	}

	public static InjectionDTO build(Injection injection) {
		return new InjectionDTO(injection);
	}
}
