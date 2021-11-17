package com.vaccine.services.vaccine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaccine.entity.Vaccine;
import com.vaccine.repositories.IVaccineRepository;
import com.vaccine.specification.vaccine.VaccineSpecificationBuilder;

@Service
public class VaccineService implements IVaccineService {

	@Autowired
	private IVaccineRepository vaccineRepository;

	@Override
	public Vaccine findById(int id) {
		return vaccineRepository.findById(id).orElse(null);
	}

	@Override
	public Vaccine findByName(String name) {
		return vaccineRepository.findByName(name);
	}

	@Override
	public Page<Vaccine> findAll(Pageable pageable, String search) {
		VaccineSpecificationBuilder specification = new VaccineSpecificationBuilder(search);
		return vaccineRepository.findAll(specification.build(), pageable);
	}

	@Override
	public void deleteById(Integer id) {
		vaccineRepository.deleteById(id);
	}

	@Override
	public Vaccine saveOrUpdate(Vaccine vaccine) {
		return vaccineRepository.save(vaccine);
	}

	@Override
	public boolean existsById(int id) {
		return vaccineRepository.existsById(id);
	}

	@Override
	public boolean existsByName(String name) {
		return vaccineRepository.existsByName(name);
	}
}
