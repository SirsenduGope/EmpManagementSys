package com.management.employee.service;

import org.springframework.http.ResponseEntity;

import com.management.employee.payload.LoginRequest;
import com.management.employee.payload.SignupRequest;

public interface IAuthService {

	public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

	public ResponseEntity<?> registerUser(SignupRequest signUpRequest) throws Exception;

}
