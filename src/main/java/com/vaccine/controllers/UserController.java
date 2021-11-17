package com.vaccine.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vaccine.constants.SecurityConstants;
import com.vaccine.dto.AccountDTO;
import com.vaccine.entity.Account;
import com.vaccine.entity.Injection;
import com.vaccine.entity.Role;
import com.vaccine.form.UserFormForUpdating;
import com.vaccine.payloads.request.RegisterRequest;
import com.vaccine.payloads.response.ErrorResponse;
import com.vaccine.payloads.response.SuccessResponse;
import com.vaccine.services.injection.IInjectionService;
import com.vaccine.services.role.IRoleService;
import com.vaccine.services.user.IUserService;

@RestController
@RequestMapping(value = "api/v1/users")
@CrossOrigin("*")
public class UserController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IInjectionService injectionService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private IRoleService roleService;

	// GET LIST USERS
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<?> getAllUsers(
			Pageable pageable,
			@RequestParam(required = false) String search) {
		Page<Account> entities = userService.findAll(pageable, search);
		Page<AccountDTO> users = entities.map(new Function<Account, AccountDTO>() {
			@Override
			public AccountDTO apply(Account account) {
				return AccountDTO.build(account);
			}
		});

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("users", users.getContent());
		response.put("totalElements", users.getTotalElements());
		response.put("totalPage", users.getTotalPages());
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get All Users", response));
	}

	// GET USER BY ID
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getUserByID(@PathVariable(name = "id") short id) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("user", userService.findById(id));
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get User By ID", response));
	}

	// UPDATE USER
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateUser(@PathVariable(name = "id") short id,
			@Valid @RequestBody UserFormForUpdating form) {
		Account account = userService.findById(id);
		if (!form.getPhone().equals(account.getPhone())) {
			if (userService.existsByPhone(form.getPhone()))
				return ResponseEntity.badRequest()
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "This Phone Number is already taken!", null));
		}
		userService.updatebyId(id, form);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Update User Successfully", null));
	}

	// CREATE NEW USER
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@PostMapping()
	public ResponseEntity<?> createUser(@RequestBody @Valid RegisterRequest request) {
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
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "CitizenId is already taken!", null));

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

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Create User Sucessfully", null));
	}

	// DELETE USER only ROOT permission can access this entry. Be careful when
	// using this method. By using this method, data integrity is lost.
	@PreAuthorize("hasRole('ROOT')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(name = "id") int id) {
		if (!userService.existsById(id))
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "User ID not found!", null));
		// If exists
		// Fetch all records in table [injection_history] and delete them
		List<Injection> injections = injectionService.findAllInjectionByUserId(Integer.valueOf(id));
		injectionService.deleteAll(injections);
		userService.deleteById(id);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Delete User Successfully", null));
	}
}
