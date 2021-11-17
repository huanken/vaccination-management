package com.vaccine.utils;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vaccine.constants.SecurityConstants;
import com.vaccine.services.auth.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {
	private final String jwtSecret = SecurityConstants.JWT_SECRET;
	private final int jwtExpiration = SecurityConstants.JWT_EXPIRATION;

	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUsernameFromJWT(String jwt) {
		return Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();
	}

	public boolean validateJWT(String jwt) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
