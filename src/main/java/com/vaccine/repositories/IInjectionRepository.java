package com.vaccine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vaccine.entity.Injection;

@Repository
public interface IInjectionRepository extends JpaRepository<Injection, Integer>, JpaSpecificationExecutor<Injection> {

}
