package com.example.jwtthuchanh.common;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.jwtthuchanh.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.slf4j.LoggerFactory;

@Component
public class JwtUntils {
	
	private static final Logger Logger = LoggerFactory.getLogger(JwtUntils.class);
	
	@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String  generateJWTToken(Authentication authentication) {
		
		UserDetailsImpl userPricipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject((userPricipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			Logger.error("Invalid JWT signature: {}", e.getMessage());
		}catch (MalformedJwtException e) {
			Logger.error("Invalid JWT token: {}", e.getMessage());
		}catch (ExpiredJwtException e) {
			Logger.error("JWT token is expired: {}", e.getMessage());
		}catch (UnsupportedJwtException e) {
			Logger.error("JWT token is unsupported: {}", e.getMessage());
		}catch (IllegalArgumentException e) {
			Logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
		
	}
	

}
