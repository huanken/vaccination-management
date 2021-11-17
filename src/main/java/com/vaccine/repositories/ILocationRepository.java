package com.vaccine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vaccine.entity.Location;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {

	Location findByName(String name);

	boolean existsByName(String name);
}
