package com.vaccine.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vaccine.services.auth.UserDetailsServiceImpl;
import com.vaccine.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);
			if (jwt != null && jwtUtils.validateJWT(jwt)) {
				String username = jwtUtils.getUsernameFromJWT(jwt);
				log.info("Username from JWT: {}", username);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails.getUsername(), null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("Cannot set user authentication: {}", e.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		log.info("Authorization: {}", authHeader);
		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

}
