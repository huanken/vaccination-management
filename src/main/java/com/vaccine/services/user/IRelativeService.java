package com.vaccine.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaccine.entity.Relative;

public interface IRelativeService {
	Relative findById(int id);

	Page<Relative> findAll(Pageable pageable, String search, String filter);

	Page<Relative> findAll(Pageable pageable, Integer userId);

	Relative saveOrUpdate(Relative family);

	void deleteById(int id);

	boolean existsById(int id);

	boolean existsByPhone(String phone);

	boolean existsByCitizenId(String citizendId);
}
