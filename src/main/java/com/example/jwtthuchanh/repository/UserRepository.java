package com.example.jwtthuchanh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jwtthuchanh.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUserName(String username);
	
	Boolean existsByUserName(String username);
	
	Boolean existsByEmail(String email);
	
	Boolean findUserByUserName(String username);
	

}
