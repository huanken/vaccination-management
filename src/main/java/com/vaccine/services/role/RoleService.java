package com.vaccine.services.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaccine.entity.Role;
import com.vaccine.repositories.IRoleRepository;

@Service
public class RoleService implements IRoleService {
	@Autowired
	private IRoleRepository roleRepository;

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public Role saveOrUpdate(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

}
