package com.vaccine.services.injection;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaccine.entity.Injection;

public interface IInjectionService {
	Injection findById(Integer id);

	Page<Injection> findAll(Pageable pageable, Integer userId);

	Page<Injection> findAll(Pageable pageable, String search, String filter);

	List<Injection> findAllInjectionByLocationId(Integer id);

	List<Injection> findAllInjectionByUserId(Integer id);

	List<Injection> findAllInjectionByVaccineId(Integer id);

	Injection saveOrUpdate(Injection injection);

	boolean existsById(int id);

	void deleteById(int id);

	void deleteAll(List<Injection> entities);
}
