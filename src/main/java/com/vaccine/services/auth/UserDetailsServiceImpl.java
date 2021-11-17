package com.vaccine.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vaccine.entity.Account;
import com.vaccine.services.user.IUserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private IUserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account user = userService.findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException("Not found username is " + username);

		return UserDetailsImpl.build(user);
	}

}
