package com.vaccine.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vaccine.entity.Vaccine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDTO {
	private int id;
	private String name;
	private String description;
	private float price;
	private int amount;
	private String manufacture;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date expiryDate;


	public VaccineDTO(Vaccine vaccine) {
		this.id = vaccine.getId();
		this.name = vaccine.getName();
		this.amount = vaccine.getAmount();
		this.expiryDate = vaccine.getExpiryDate();
		this.description = vaccine.getDescription();
		this.price = vaccine.getPrice();
		this.manufacture = vaccine.getManufacture();
	}

	public static VaccineDTO build(Vaccine vaccine) {
		return new VaccineDTO(vaccine);
	}
}
