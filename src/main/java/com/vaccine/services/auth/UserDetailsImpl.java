package com.vaccine.services.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaccine.entity.Role;
import com.vaccine.services.auth.UserDetailsImpl;
import com.vaccine.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	private Account user;

	public static UserDetailsImpl build(Account user) {
		return new UserDetailsImpl(user);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> grants = new HashSet<GrantedAuthority>();
		for (Role role : user.getRoles()) {
			grants.add(new SimpleGrantedAuthority(role.getName()));
		}
		return grants;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public String getFullname() {
		return user.getFullname();
	}
}
