package com.vaccine.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vaccine.dto.InjectionDTO;
import com.vaccine.entity.Account;
import com.vaccine.entity.Injection;
import com.vaccine.entity.Location;
import com.vaccine.entity.Vaccine;
import com.vaccine.payloads.request.InjectionRequest;
import com.vaccine.payloads.response.ErrorResponse;
import com.vaccine.payloads.response.SuccessResponse;
import com.vaccine.services.injection.IInjectionService;
import com.vaccine.services.location.ILocationService;
import com.vaccine.services.user.IUserService;
import com.vaccine.services.vaccine.IVaccineService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/injections")
@CrossOrigin("*")
@Slf4j
public class InjectionController {
	@Autowired
	private IInjectionService injectionService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IVaccineService vaccineService;
	@Autowired
	private ILocationService locationService;

	/**
	 * Get all injections (injection_history), use for ADMIN page (MOD page). Only
	 * MOD or ADMIN permission can access this entry.
	 * 
	 * @param pageable
	 * @param userId
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getAllInjection(Pageable pageable,
			@RequestParam(required = false, defaultValue = " ") String search,
			@RequestParam(required = false, defaultValue = " ") String filter) {
		List<InjectionDTO> injections = new ArrayList<InjectionDTO>();
		Page<Injection> records = injectionService.findAll(pageable, search, filter);
		records.forEach(injection -> injections.add(InjectionDTO.build(injection)));

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("injections", injections);
		response.put("totalElements", records.getTotalElements());
		response.put("totalPage", records.getTotalPages());

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get all injection", response));
	}

	/**
	 * Get all injection history by user id. USER, MOD and ADMIN can access this
	 * entry.
	 * 
	 * @param pageable
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllInjectionByUserId(Pageable pageable, @PathVariable int id) {
		Page<Injection> records = injectionService.findAll(pageable, id);

		Page<InjectionDTO> injections = records.map(new Function<Injection, InjectionDTO>() {
			@Override
			public InjectionDTO apply(Injection injection) {
				return InjectionDTO.build(injection);
			}
		});

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("injections", injections.toList());
		response.put("totalElements", records.getTotalElements());
		response.put("totalPage", records.getTotalPages());

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get all injection from userId = " + id, response));
	}

	/**
	 * Get injection by ID. MOD and ADMIN can access this entry.
	 * 
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getInjectionById(@PathVariable(name = "id") int id) {
		// Check if injection ID exists
		if (!injectionService.existsById(id))
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Injection ID not found!", null));

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("injection", injectionService.findById(id));
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get injection by ID", response));
	}

	/**
	 * Create an injection history, only MOD and ADMIN can access this entry.
	 * 
	 * @param request
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createInjection(@Valid @RequestBody InjectionRequest request) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dateInjection = new Date();
		try {
			dateInjection = simpleDateFormat.parse(request.getDateInjection());
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		if (dateInjection.equals(new Date()) || dateInjection.before(new Date()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Date injection must be in the future!", null));

		// Get user object from authentication
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Account user = userService.findByUsername(username);

		// Get vaccine object from vaccine_name
		Vaccine vaccine = vaccineService.findByName(request.getVaccineName());
		if (vaccine == null)
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Vaccine not found!", null));

		// Get location objection from location_name
		Location location = locationService.findByName(request.getLocationName());
		if (location == null)
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Location not found!", null));

		// Create injection object
		Injection injection = new Injection(0, user, vaccine, location, dateInjection, request.getStatus());

		injectionService.saveOrUpdate(injection);

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Create injection success", null));
	}

	/**
	 * Change status of injection to "Đã tiêm"
	 * 
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateInjectionStatus(@PathVariable Integer id) {
		Injection injection = injectionService.findById(id);
		if (injection == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Injection ID not found!", null));

		// Check if dateInjection in the past
		if (injection.getDateInjection().after(new Date()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Date injection has not come!", null));

		injection.setStatus("Đã tiêm");
		injectionService.saveOrUpdate(injection);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Update injection status success!", null));
	}

	/**
	 * Delete injection history, only ROOT permission can access this entry. Be
	 * careful when using this method. By using this method, data integrity is lost.
	 * 
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('ROOT')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteInjection(@PathVariable int id) {
		// Check if injection id exists
		if (!injectionService.existsById(id))
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Injection ID not found!", null));
		// If exists, let delete it
		injectionService.deleteById(id);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Delete injection success!", null));
	}

}
