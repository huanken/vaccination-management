package com.vaccine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vaccine.entity.Account;

@Repository
public interface IUserRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account>  {
	Account findByUsername(String username);

	boolean existsByPhone(String phone);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByCitizenId(String citizendId);
}
