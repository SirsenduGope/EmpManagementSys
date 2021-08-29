package com.management.employee.serviceImpl;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.management.employee.CustomUserDetails;
import com.management.employee.entity.Employee;
import com.management.employee.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private EmployeeRepository repo;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Employee> user = repo.findByEmail(email);
		if(user.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}
		return CustomUserDetails.build(user.get());
	}

}
