package com.hr.user.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hr.user.entities.User;
import com.hr.user.repositories.UserRepository;


@RestController
@RequestMapping("/users")
public class UserResource {

	@Autowired
	private UserRepository repository;

	@GetMapping("/{id}")
	private ResponseEntity<User> findById(@PathVariable Long id) {
		User obj = repository.findById(id).get();
		
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping("/search")
	private ResponseEntity<User> findByEmail(@RequestParam String email) {
		User obj = repository.findByEmail(email);
		
		return ResponseEntity.ok(obj);
	}
}