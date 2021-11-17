package com.vaccine.payloads.response;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaccine.services.auth.UserDetailsImpl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private String username;
	private String accessToken;
	private String fullname;
	private List<String> roles = new ArrayList<String>();

	public LoginResponse(UserDetailsImpl userDetails, String jwt) {
		this.username = userDetails.getUsername();
		this.fullname = userDetails.getFullname();
		this.roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority())
				.collect(Collectors.toList());
		this.accessToken = jwt;
	}
}
