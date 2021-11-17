package com.vaccine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vaccine.entity.Vaccine;


@Repository
public interface IVaccineRepository extends JpaRepository<Vaccine, Integer>, JpaSpecificationExecutor<Vaccine>  {

	Vaccine findByName(String name);

	boolean existsByName(String name);
}
