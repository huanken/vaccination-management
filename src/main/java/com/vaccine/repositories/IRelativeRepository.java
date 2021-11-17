package com.vaccine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vaccine.entity.Relative;

@Repository
public interface IRelativeRepository extends JpaRepository<Relative, Integer>, JpaSpecificationExecutor<Relative> {
	boolean existsByPhone(String phone);

	boolean existsByCitizenId(String citizendId);
}
