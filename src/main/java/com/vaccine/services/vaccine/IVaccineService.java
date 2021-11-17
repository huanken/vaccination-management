package com.vaccine.services.vaccine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaccine.entity.Vaccine;

public interface IVaccineService {
	Vaccine findById(int id);

	Vaccine findByName(String name);

	Page<Vaccine> findAll(Pageable pageable, String search);

	Vaccine saveOrUpdate(Vaccine vaccine);

	void deleteById(Integer id);

	boolean existsById(int id);

	boolean existsByName(String name);

}
