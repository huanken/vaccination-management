package com.vaccine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vaccine.entity.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
	Role findByName(String name);
}
