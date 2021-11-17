package com.vaccine.services.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaccine.entity.Location;

public interface ILocationService {
	Location findById(int id);

	Location findByName(String name);

	Page<Location> findAll(Pageable pageable, String search);

	Location saveOrUpdate(Location location);

	void deleteById(Integer id);

	boolean existsById(int id);

	boolean existsByName(String name);

}
