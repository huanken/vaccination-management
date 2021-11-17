package com.vaccine.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vaccine.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class UserFormForUpdating {
	@NotEmpty(message = "Fullname name must be not empty!")
	@Size(min = 5, max = 255, message = "Length of Fullname name must be 5 to 255.")
	private String fullname;

	@NotNull(message = "Age must be not null!")
	@Min(value = 12, message = "Age must be higher 12.")
	private Integer age;

	@NotEmpty(message = "Address name must be not empty!")
	@Size(min = 5, max = 255, message = "Length of address name must be 5 to 255.")
	private String address;

	@NotEmpty(message = "Phone name must be not empty!")
	private String phone;

	@NotEmpty(message = "CitizenId name must be not empty!")
	private String citizenId;

	@NotNull(message = "Prioritize name must be not null!")
	private int prioritize;

	public UserFormForUpdating(Account account) {
		this.fullname = account.getFullname();
		this.age = account.getAge();
		this.address = account.getAddress();
		this.phone = account.getPhone();
		this.citizenId = account.getCitizenId();
		this.prioritize = account.getPrioritize();
	}
}
