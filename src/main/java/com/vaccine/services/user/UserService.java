package com.vaccine.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaccine.entity.Account;
import com.vaccine.form.UserFormForUpdating;
import com.vaccine.repositories.IUserRepository;
import com.vaccine.specification.user.UserSpecificationBuilder;

@Service
public class UserService implements IUserService {
	@Autowired
	private IUserRepository userRepository;

	@Override
	public Account findById(int id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public Account findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Page<Account> findAll(Pageable pageable, String search) {
		UserSpecificationBuilder specification = new UserSpecificationBuilder(search);
		return userRepository.findAll(specification.build(), pageable);
	}

	@Override
	public Account saveOrUpdate(Account user) {
		return userRepository.save(user);
	}

	@Override
	public void updatebyId(int id, UserFormForUpdating form) {
		Account account = userRepository.findById(id).get();
		account.setFullname(form.getFullname());
		account.setAge(form.getAge());
		account.setAddress(form.getAddress());
		account.setPhone(form.getPhone());
		account.setCitizenId(form.getCitizenId());
		account.setPrioritize(form.getPrioritize());
		userRepository.save(account);
	}

	@Override
	public void deleteById(int id) {
		userRepository.deleteById(id);
	}

	@Override
	public boolean existsById(int id) {
		return userRepository.existsById(id);
	}

	@Override
	public boolean existsByPhone(String phone) {
		return userRepository.existsByPhone(phone);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByCitizenId(String citizendId) {
		return userRepository.existsByCitizenId(citizendId);
	}

}
