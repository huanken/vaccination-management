package com.vaccine.services.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaccine.entity.Location;
import com.vaccine.repositories.ILocationRepository;
import com.vaccine.specification.location.LocationSpecification;

@Service
public class LocationService implements ILocationService {
	@Autowired
	private ILocationRepository locationRepository;

	@Override
	public Location findById(int id) {
		return locationRepository.findById(id).orElse(null);
	}

	@Override
	public Location findByName(String name) {
		return locationRepository.findByName(name);
	}

	@Override
	public Page<Location> findAll(Pageable pageable, String search) {
		// If search is a number, searching by location id
		if (search.matches("^(\\d+)$")) {
			return locationRepository.findAll(LocationSpecification.whereLocationIdEqual(Integer.valueOf(search)),
					pageable);
		}
		// Default search by name
		return locationRepository.findAll(LocationSpecification.whereLocationNameLike(search), pageable);
	}

	@Override
	public Location saveOrUpdate(Location location) {
		return locationRepository.save(location);
	}

	@Override
	public void deleteById(Integer id) {
		locationRepository.deleteById(id);
	}

	@Override
	public boolean existsById(int id) {
		return locationRepository.existsById(id);
	}

	@Override
	public boolean existsByName(String name) {
		return locationRepository.existsByName(name);
	}
}
