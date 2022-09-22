package com.example.jwtthuchanh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication
public class JwtthuchanhApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtthuchanhApplication.class, args);
	}

}
