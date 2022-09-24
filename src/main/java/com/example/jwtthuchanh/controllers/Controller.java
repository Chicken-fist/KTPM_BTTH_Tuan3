package com.example.jwtthuchanh.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtthuchanh.common.ERole;
import com.example.jwtthuchanh.dto.MessageResponse;
import com.example.jwtthuchanh.dto.SignupRequest;
import com.example.jwtthuchanh.dto.UserDto;
import com.example.jwtthuchanh.entity.Role;
import com.example.jwtthuchanh.entity.User;
import com.example.jwtthuchanh.repository.RoleRepository;
import com.example.jwtthuchanh.repository.UserRepository;

@CrossOrigin(maxAge = 3000)
@RestController
@RequestMapping("/api")
public class Controller {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;

	@GetMapping("/get-users")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')") 
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	@PutMapping("/admin/update-users/{id}")
	@PreAuthorize("hasRole('ADMIN')") 
	public ResponseEntity<?> update(@PathVariable("id") Long id, @Validated @RequestBody UserDto userDto){
		
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: user dont exists"));
		
		user.setUserName(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(encoder.encode(userDto.getPassword()));
		
		userRepository.save(user);
		return ResponseEntity.ok().body(user);
	}
	
	@DeleteMapping("/admin/delete-users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id){
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found user id: " + id));
		userRepository.deleteById(user.getId());
		return ResponseEntity.ok().body("Success");
	}
	
	@PostMapping("/admin/add-users")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUserName(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
		}

		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()));

		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<Role>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin": {
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(adminRole);

				}
				case "ADMIN": {
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(adminRole);

				}
				

				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(userRole);
				}
			});
			
		}
		user.setRoles(roles);
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("success"));
	}
	
}
