package com.vaccine.controllers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccine.constants.SecurityConstants;
import com.vaccine.entity.Role;
import com.vaccine.entity.Account;
import com.vaccine.payloads.request.LoginRequest;
import com.vaccine.payloads.request.RegisterRequest;
import com.vaccine.payloads.response.ErrorResponse;
import com.vaccine.payloads.response.LoginResponse;
import com.vaccine.payloads.response.SuccessResponse;
import com.vaccine.services.auth.UserDetailsImpl;
import com.vaccine.services.role.IRoleService;
import com.vaccine.services.user.IUserService;
import com.vaccine.utils.JwtUtils;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private JwtUtils jwtUtils;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		// If ok, generate jwt
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> response = objMapper.convertValue(new LoginResponse(userDetails, jwt),
				new TypeReference<Map<String, Object>>() {
				});

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Login success", response));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
		if (userService.existsByPhone(request.getPhone()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Phone number is already taken!", null));
		if (userService.existsByUsername(request.getUsername()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Username is already taken!", null));
		if (userService.existsByEmail(request.getEmail()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Email is already taken!", null));
		if (userService.existsByCitizenId(request.getCitizenId()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "CitizenID is already taken!", null));

		Account account = new Account();
		account.setUsername(request.getUsername());
		account.setPassword(passwordEncoder.encode(request.getPassword()));
		account.setFullname(request.getFullname());
		account.setAge(request.getAge());
		account.setAddress(request.getAddress());
		account.setPhone(request.getPhone());
		account.setEmail(request.getEmail());
		account.setCitizenId(request.getCitizenId());
		if (request.getPrioritize() != 0)
			account.setPrioritize(request.getPrioritize());

		Set<Role> roles = new HashSet<Role>();
		if (request.getRoles() == null || request.getRoles().length == 0) {
			Role role = roleService.findByName(SecurityConstants.ROLE_USER);
			if (role != null)
				roles.add(role);
			else {
				Role newRole = new Role();
				newRole.setName(SecurityConstants.ROLE_USER);
				if ((role = roleService.saveOrUpdate(newRole)) != null)
					roles.add(role);
			}
		} else {
			for (String strRole : request.getRoles()) {
				switch (strRole) {
				case SecurityConstants.ROLE_ADMIN:
					Role adminRole = roleService.findByName(SecurityConstants.ROLE_ADMIN);
					if (adminRole != null)
						roles.add(adminRole);
					else {
						Role newRole = new Role();
						newRole.setName(SecurityConstants.ROLE_ADMIN);
						if ((adminRole = roleService.saveOrUpdate(newRole)) != null)
							roles.add(adminRole);
					}
					break;
				case SecurityConstants.ROLE_MOD:
					Role modRole = roleService.findByName(SecurityConstants.ROLE_MOD);
					if (modRole != null)
						roles.add(modRole);
					else {
						Role newRole = new Role();
						newRole.setName(SecurityConstants.ROLE_MOD);
						if ((modRole = roleService.saveOrUpdate(newRole)) != null)
							roles.add(modRole);
					}
					break;
				default:
					Role userRole = roleService.findByName(SecurityConstants.ROLE_USER);
					if (userRole != null)
						roles.add(userRole);
					else {
						Role newRole = new Role();
						newRole.setName(SecurityConstants.ROLE_USER);
						if ((userRole = roleService.saveOrUpdate(newRole)) != null)
							roles.add(userRole);
					}
					break;
				}
			}
		}
		account.setRoles(roles);

		userService.saveOrUpdate(account);

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Register success", null));
	}
}
