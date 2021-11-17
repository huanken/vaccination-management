package com.vaccine.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaccine.entity.Account;
import com.vaccine.form.UserFormForUpdating;

public interface IUserService {
	Account findById(int id);

	Account findByUsername(String username);

	Page<Account> findAll(Pageable pageable, String search);

	Account saveOrUpdate(Account user);

	void updatebyId(int id, UserFormForUpdating form);

	void deleteById(int id);

	boolean existsById(int id);

	boolean existsByPhone(String phone);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByCitizenId(String citizendId);
}
