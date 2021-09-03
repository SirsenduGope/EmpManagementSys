package com.management.employee.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.management.employee.CustomUserDetails;
import com.management.employee.controller.AuthController;
import com.management.employee.entity.Employee;
import com.management.employee.entity.EmployeeDetails;
import com.management.employee.entity.Role;
import com.management.employee.enums.Roles;
import com.management.employee.payload.JwtResponse;
import com.management.employee.payload.LoginRequest;
import com.management.employee.payload.Message;
import com.management.employee.payload.SignupRequest;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.repository.RoleRepository;
import com.management.employee.security.jwt.JwtUtils;
import com.management.employee.service.IAuthService;

@Service
public class AuthServiceImpl implements IAuthService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Override
	public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getEmployee().getId(), 
												 userDetails.getEmployee().getEmail(), 
												 roles));
	}

	@Override
	public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
		if (employeeRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Error: Email is already in use!"));
		}

		// Create new user's account
		Employee user = new Employee(signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));
		
		EmployeeDetails empDetails = new EmployeeDetails();

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByRole(Roles.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByRole(Roles.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					
					signUpRequest.setReportTo(signUpRequest.getEmail());

					break;
				case "hr":
					Role hrRole = roleRepository.findByRole(Roles.ROLE_HR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(hrRole);

					break;
				case "manager":
					Role managerRole = roleRepository.findByRole(Roles.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(managerRole);

					break;
				default:
					Role userRole = roleRepository.findByRole(Roles.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		try {
			user.setRoles(roles);
			empDetails.setFirstName(signUpRequest.getFirstName());
			empDetails.setLastName(signUpRequest.getLastName());
			
			if(!roles.iterator().next().getRole().name().equals(Roles.ROLE_ADMIN.name())) {
				if(signUpRequest.getReportTo() != null) {
					try {
						Optional<Employee> reportTo = employeeRepository.findByEmail(signUpRequest.getReportTo());
						if(reportTo.isPresent()) {
							user.setManagerEmail(reportTo.get().getEmail());
						}
						else {
							logger.debug("No user found for email id : " + signUpRequest.getReportTo());
						}
					}catch(Exception ex) {
						logger.debug("ERROR : On fetching employee for email : " + signUpRequest.getReportTo());
						logger.debug("ERROR : Message : " + ex.getMessage());
					}
				}
				else {
					return new ResponseEntity<Message>(new Message("The user is not reporting to anyone."), HttpStatus.BAD_REQUEST);
				}
			}
			else {
				user.setManagerEmail(signUpRequest.getReportTo());
			}
			
			user.setEmployeeDetails(empDetails);
			employeeRepository.save(user);
		}catch(IllegalArgumentException ex) {
			logger.debug("Illigal argument exception on saving new employee, "+ex.getMessage());
		}
		catch(Exception ex) {
			logger.debug("ERROR : Exception on new employee register : " + ex.getMessage());
			logger.debug("Exception : " + ex.getStackTrace());
		}

		return ResponseEntity.ok(new Message("User registered successfully!"));
	}
}
