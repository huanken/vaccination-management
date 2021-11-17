package com.vaccine.dto;

import com.vaccine.entity.Relative;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RelativeDTO {
	private int id;
	private AccountDTO reference;
	private String fullname;
	private int age;
	private String address;
	private String phone;
	private String citizenId;
	private int prioritize;

	public RelativeDTO(Relative family) {
		this.id = family.getId();
		this.reference = AccountDTO.build(family.getAccount());
		this.fullname = family.getFullname();
		this.age = family.getAge();
		this.address = family.getAddress();
		this.phone = family.getPhone();
		this.citizenId = family.getCitizenId();
		this.prioritize = family.getPrioritize();
	}

	public static RelativeDTO build(Relative family) {
		return new RelativeDTO(family);
	}
}
