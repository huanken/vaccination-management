package com.vaccine.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.vaccine.dto.VaccineDTO;
import com.vaccine.entity.Injection;
import com.vaccine.entity.Vaccine;
import com.vaccine.payloads.request.VaccineRequest;
import com.vaccine.payloads.response.ErrorResponse;
import com.vaccine.payloads.response.SuccessResponse;
import com.vaccine.services.injection.IInjectionService;
import com.vaccine.services.vaccine.IVaccineService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "api/v1/vaccines")
@CrossOrigin("*")
@Slf4j

public class VaccineController {
	@Autowired
	private IVaccineService vaccineService;
	@Autowired
	private IInjectionService injectionService;

	// GET LIST VACCINES
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<?> getAllVaccines(
			Pageable pageable,
			@RequestParam(required = false) String search) {
		Page<Vaccine> entities = vaccineService.findAll(pageable, search);
		Page<VaccineDTO> vaccines = entities.map(new Function<Vaccine, VaccineDTO>() {
			@Override
			public VaccineDTO apply(Vaccine vaccine) {
				return VaccineDTO.build(vaccine);
			}
		});
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("vaccines", vaccines.getContent());
		response.put("totalElements", vaccines.getTotalElements());
		response.put("totalPage", vaccines.getTotalPages());
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get All Vaccines", response));
	}

	// CREATE NEW VACCINE
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewVaccine(@Valid @RequestBody VaccineRequest request) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date expiryDate = new Date();
		try {
			expiryDate = simpleDateFormat.parse(request.getExpiryDate());
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		if (expiryDate.equals(new Date()) || expiryDate.before(new Date()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Expiry Date must be in the future!", null));
		if (vaccineService.existsByName(request.getVaccineName()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Vaccine name is exists!", null));

		vaccineService.saveOrUpdate(new Vaccine(0, request.getVaccineName(), request.getDescription(),
				request.getPrice(), request.getAmount(),
				expiryDate, request.getManufacture()));

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Create vaccine success!", null));
	}

	// GET VACCINE BY ID
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getVaccineByID(@PathVariable(name = "id") short id) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("vaccine", VaccineDTO.build(vaccineService.findById(id)));
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Get Vaccine By ID", response));
	}

	// UPDATE VACCINE
	@PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateVaccine(@PathVariable Integer id, @Valid @RequestBody VaccineRequest request) {
		Vaccine vaccine = vaccineService.findById(id);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date expiryDate = new Date();
		try {
			expiryDate = simpleDateFormat.parse(request.getExpiryDate());
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		if (expiryDate.equals(new Date()) || expiryDate.before(new Date()))
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Expiry Date must be in the future!", null));
		if (vaccine == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Vaccine ID not found!", null));

		vaccine.setName(request.getVaccineName());
		vaccine.setDescription(request.getDescription());
		vaccine.setPrice(request.getPrice());
		vaccine.setAmount(request.getAmount());
		vaccine.setManufacture(request.getManufacture());
		vaccine.setExpiryDate(expiryDate);
		vaccineService.saveOrUpdate(vaccine);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Update vaccine status success!", null));
	}

	// DELETE VACCINE only ROOT permission can access this entry. Be careful when
	// using this method. By using this method, data integrity is lost.
	@PreAuthorize("hasRole('ROOT')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteVaccine(@PathVariable int id) {
		// Check if location id exists
		if (!vaccineService.existsById(id))
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Vaccine ID not found!", null));
		// If exists
		// Fetch all records in table [injection_history] and delete them
		List<Injection> injections = injectionService.findAllInjectionByVaccineId(Integer.valueOf(id));
		injectionService.deleteAll(injections);
		// Delete vaccine
		vaccineService.deleteById(id);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "Delete vaccine success!", null));
	}
}
