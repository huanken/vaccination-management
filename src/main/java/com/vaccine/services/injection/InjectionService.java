package com.vaccine.services.injection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vaccine.entity.Injection;
import com.vaccine.repositories.IInjectionRepository;
import com.vaccine.specification.injection.InjectionSpecification;

@Service
public class InjectionService implements IInjectionService {
	@Autowired
	private IInjectionRepository injectionRepository;

	@Override
	public Page<Injection> findAll(Pageable pageable, Integer userId) {
		return injectionRepository.findAll(InjectionSpecification.whereUserIdEqual(userId), pageable);
	}

	@Override
	public Page<Injection> findAll(Pageable pageable, String search, String filter) {
		Specification<Injection> specs = InjectionSpecification.searchBuilder(search)
				.and(InjectionSpecification.filterBuilder(filter));

		return injectionRepository.findAll(specs, pageable);
	}

	@Override
	public List<Injection> findAllInjectionByLocationId(Integer id) {
		return injectionRepository.findAll(InjectionSpecification.whereLocationIdEqual(id));
	}

	@Override
	public List<Injection> findAllInjectionByUserId(Integer id) {
		return injectionRepository.findAll(InjectionSpecification.whereUserIdEqual(id));
	}

	@Override
	public List<Injection> findAllInjectionByVaccineId(Integer id) {
		return injectionRepository.findAll(InjectionSpecification.whereVaccineIdEqual(id));
	}

	@Override
	public Injection saveOrUpdate(Injection injection) {
		return injectionRepository.save(injection);
	}

	@Override
	public Injection findById(Integer id) {
		return injectionRepository.findById(id).orElse(null);
	}

	@Override
	public boolean existsById(int id) {
		return injectionRepository.existsById(id);
	}

	@Override
	public void deleteById(int id) {
		injectionRepository.deleteById(id);
	}

	@Override
	public void deleteAll(List<Injection> entities) {
		injectionRepository.deleteAll(entities);
	}
}
