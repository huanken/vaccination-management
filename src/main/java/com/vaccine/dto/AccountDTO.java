package com.vaccine.dto;

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
public class AccountDTO {
	private int id;
	private String username;
	private String fullname;
	private int age;
	private String address;
	private String phone;
	private String email;
	private String citizenId;
	private int prioritize;

	public AccountDTO(Account account) {
		this.id = account.getId();
		this.username = account.getUsername();
		this.fullname = account.getFullname();
		this.age = account.getAge();
		this.address = account.getAddress();
		this.phone = account.getPhone();
		this.email = account.getEmail();
		this.citizenId = account.getCitizenId();
		this.prioritize = account.getPrioritize();
	}

	public static AccountDTO build(Account account) {
		return new AccountDTO(account);
	}
}
