package com.vaccine.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vaccine.entity.Injection;
import com.vaccine.entity.Location;
import com.vaccine.payloads.request.LocationRequest;
import com.vaccine.payloads.response.ErrorResponse;
import com.vaccine.payloads.response.SuccessResponse;
import com.vaccine.services.injection.IInjectionService;
import com.vaccine.services.location.ILocationService;

@RestController
@RequestMapping("/api/v1/locations")
@CrossOrigin("*")
public class LocationController {
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IInjectionService injectionService;

	/**
	 * Get all location, only MOD or ADMIN permission can access this entry.
	 * 
	 * @param pageable
	 * @param search
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllLocation(Pageable pageable,
			@RequestParam(required = false, defaultValue = "") String search) {
		Page<Location> records = locationService.findAll(pageable, search);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("locations", records.getContent());
		response.put("totalElements", records.getTotalElements());
		response.put("totalPage", records.getTotalPages());

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get all location", response));
	}

	/**
	 * Create new location, only MOD or ADMIN permission can access this entry.
	 * 
	 * @param location
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewLocation(@Valid @RequestBody LocationRequest request) {
		// Check if location name is exists
		if (locationService.existsByName(request.getLocationName()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Location name is exists!", null));
		// Everything ok, let create new location
		locationService.saveOrUpdate(new Location(0, request.getLocationName()));
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Create location success!", null));
	}

	// GET LOCATION BY ID
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getLocationByID(@PathVariable(name = "id") short id) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("location", locationService.findById(id));
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get Location By ID", response));
	}

	/**
	 * Update location, only MOD or ADMIN permission can access this entry.
	 * 
	 * @param id
	 * @param locationName
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLocation(@PathVariable int id, @Valid @RequestBody LocationRequest request) {
		// Get instance of location
		Location location = locationService.findById(id);
		// Check if location id is exists
		if (location == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Location ID not found!", null));
		// Check if new location equal old location
		if (!request.getLocationName().equals(location.getName())) {
			location.setName(request.getLocationName());
			locationService.saveOrUpdate(location);
		}
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Update location success!", null));
	}

	/**
	 * Delete location, only ROOT permission can access this entry. Be careful when
	 * using this method. By using this method, data integrity is lost.
	 * 
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PreAuthorize("hasRole('ROOT')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLocation(@PathVariable int id) {
		// Check if location id exists
		if (!locationService.existsById(id))
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Location ID not found!", null));
		// If exists
		// Fetch all records in table [injection_history] and delete them
		List<Injection> injections = injectionService.findAllInjectionByLocationId(id);
		injectionService.deleteAll(injections);
		// Delete location
		locationService.deleteById(id);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Delete location success!", null));
	}
}
