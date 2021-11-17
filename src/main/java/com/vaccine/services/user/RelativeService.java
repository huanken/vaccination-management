package com.vaccine.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vaccine.entity.Relative;
import com.vaccine.repositories.IRelativeRepository;
import com.vaccine.specification.user.RelativeSpecification;

@Service
public class RelativeService implements IRelativeService {
	@Autowired
	private IRelativeRepository relativeRepository;

	@Override
	public Relative findById(int id) {
		return relativeRepository.findById(id).orElse(null);
	}

	@Override
	public Page<Relative> findAll(Pageable pageable, String search, String filter) {
		Specification<Relative> specs = RelativeSpecification.searchBuilder(search)
				.and(RelativeSpecification.filterBuilder(filter));

		return relativeRepository.findAll(specs, pageable);
	}

	@Override
	public Page<Relative> findAll(Pageable pageable, Integer refId) {
		return relativeRepository.findAll(RelativeSpecification.whereReferenceIdEqual(refId), pageable);
	}

	@Override
	public Relative saveOrUpdate(Relative family) {
		return relativeRepository.save(family);
	}

	@Override
	public void deleteById(int id) {
		relativeRepository.deleteById(id);
	}

	@Override
	public boolean existsById(int id) {
		return relativeRepository.existsById(id);
	}

	@Override
	public boolean existsByPhone(String phone) {
		return relativeRepository.existsByPhone(phone);
	}

	@Override
	public boolean existsByCitizenId(String citizendId) {
		return relativeRepository.existsByCitizenId(citizendId);
	}

}
