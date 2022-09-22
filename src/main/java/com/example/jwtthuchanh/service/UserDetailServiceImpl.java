package com.example.jwtthuchanh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.jwtthuchanh.entity.User;
import com.example.jwtthuchanh.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	UserRepository userRopository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRopository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		return UserDetailsImpl.build(user);
	}
	
	

}
