package com.example.jwtthuchanh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jwtthuchanh.common.ERole;
import com.example.jwtthuchanh.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByName(ERole name);
	
	

}
