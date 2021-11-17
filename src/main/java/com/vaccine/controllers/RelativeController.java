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

import com.vaccine.dto.RelativeDTO;
import com.vaccine.entity.Account;
import com.vaccine.entity.Relative;
import com.vaccine.payloads.request.RelativeRequest;
import com.vaccine.payloads.response.ErrorResponse;
import com.vaccine.payloads.response.SuccessResponse;
import com.vaccine.services.user.IRelativeService;
import com.vaccine.services.user.IUserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/relative")
@CrossOrigin("*")
@Slf4j
public class RelativeController {
	@Autowired
	private IRelativeService relativeService;
	@Autowired
	private IUserService userService;

	/**
	 * Get all relatives, use for ADMIN page (MOD page). Only MOD or ADMIN
	 * permission can access this entry.
	 * 
	 * @param pageable
	 * @param search
	 * @param filter
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getAllRelative(Pageable pageable,
			@RequestParam(required = false, defaultValue = " ") String search,
			@RequestParam(required = false, defaultValue = " ") String filter) {
		List<RelativeDTO> relativeDTO = new ArrayList<RelativeDTO>();
		Page<Relative> records = relativeService.findAll(pageable, search, filter);
		records.forEach(record -> relativeDTO.add(RelativeDTO.build(record)));

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("relatives", relativeDTO);
		response.put("totalElements", records.getTotalElements());
		response.put("totalPage", records.getTotalPages());

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get all relatives", response));
	}

	/**
	 * Get all relative by reference user id. USER, MOD and ADMIN can access this
	 * entry.
	 * 
	 * @param pageable
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllRelativeByUserId(Pageable pageable, @PathVariable int id) {
		Page<Relative> records = relativeService.findAll(pageable, id);

		Page<RelativeDTO> relatives = records.map(new Function<Relative, RelativeDTO>() {
			@Override
			public RelativeDTO apply(Relative family) {
				return RelativeDTO.build(family);
			}
		});

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("relatives", relatives.toList());
		response.put("totalElements", records.getTotalElements());
		response.put("totalPage", records.getTotalPages());

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get all relatives from refId = " + id, response));
	}

	/**
	 * Get relative by ID. MOD and ADMIN can access this entry.
	 * 
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getRelativeById(@PathVariable(name = "id") int id) {
		// Check if injection ID exists
		if (!relativeService.existsById(id))
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Relative ID not found!", null));

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("relative", relativeService.findById(id));
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get relaltive by ID", response));
	}

	/**
	 * Create an member of your family. USER, MOD and ADMIN can access this entry.
	 * 
	 * @param request
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createRelative(@Valid @RequestBody RelativeRequest request) {
		// Check if exists unique columns
		if (userService.existsByCitizenId(request.getCitizenId())
				|| relativeService.existsByCitizenId(request.getCitizenId()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "CitizenID is already taken!", null));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(request.getDateInjection());
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		if (date.equals(new Date()) || date.before(new Date()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Date must be in the future!", null));
		// Get reference user object from authentication
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Account account = userService.findByUsername(username);

		// Create injection object
		Relative relative = new Relative();
		relative.setAccount(account);
		relative.setFullname(request.getFullname());
		relative.setAge(request.getAge());
		relative.setAddress(request.getAddress());
		relative.setPhone(account.getPhone());
		relative.setCitizenId(request.getCitizenId());
		relative.setDate(date);
		if (request.getPrioritize() != 0)
			relative.setPrioritize(request.getPrioritize());

		relativeService.saveOrUpdate(relative);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Add new relative success", null));
	}

	/**
	 * Update relative. USER, MOD and ADMIN can access this entry.
	 * 
	 * @param id
	 * @param request
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateRelative(@PathVariable Integer id,
			@Valid @RequestBody RelativeRequest request) {
		Relative relative = relativeService.findById(id);
		if (relative == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Family ID not found!", null));

		relative.setFullname(request.getFullname());
		relative.setAge(request.getAge());
		relative.setAddress(request.getAddress());
		relative.setCitizenId(request.getCitizenId());
		relative.setPrioritize(request.getPrioritize());

		relativeService.saveOrUpdate(relative);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Update relative success!", null));
	}

	/**
	 * Delete relative.
	 * 
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRelative(@PathVariable int id) {
		// Check if relative id exists
		if (!relativeService.existsById(id))
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Relative ID not found!", null));
		// If exists, delete it
		relativeService.deleteById(id);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Delete relative success!", null));
	}

}
