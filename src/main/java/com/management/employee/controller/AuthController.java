package com.management.employee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.management.employee.payload.LoginRequest;
import com.management.employee.payload.SignupRequest;
import com.management.employee.service.IAuthService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private IAuthService authService;
	
	public AuthController(IAuthService authService) {
		this.authService = authService;
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		return authService.authenticateUser(loginRequest);
	}

	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) throws Exception {
		return authService.registerUser(signUpRequest);
	}

}
