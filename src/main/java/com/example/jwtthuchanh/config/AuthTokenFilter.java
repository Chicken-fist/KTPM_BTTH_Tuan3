package com.example.jwtthuchanh.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwtthuchanh.common.JwtUntils;
import com.example.jwtthuchanh.service.UserDetailServiceImpl;
import com.example.jwtthuchanh.service.UserDetailsImpl;

public class AuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUntils jwtUntils;
	
	@Autowired
	private UserDetailServiceImpl userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String jwt = pasreJwt(request);
			if(jwt != null && jwtUntils.validateJwtToken(jwt)) {
				String username = jwtUntils.getUserNameFromJwtToken(jwt);
				
				UserDetails userDetails = userDetailService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, 
						null, userDetails.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
			
		}
		filterChain.doFilter(request, response);
		
	}

	private String pasreJwt(HttpServletRequest request) {
		
		String headerAuth = request.getHeader("Authorization");
		
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}

}
