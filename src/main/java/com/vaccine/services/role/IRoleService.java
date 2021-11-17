package com.vaccine.services.role;

import java.util.List;

import com.vaccine.entity.Role;

public interface IRoleService {
	List<Role> findAll();
	
	Role findByName(String name);

	Role saveOrUpdate(Role role);
}
