package com.management.employee.service;

import org.springframework.http.ResponseEntity;

import com.management.employee.payload.SignupRequest;

import javassist.NotFoundException;

public interface IEmployeeService {

	public ResponseEntity<?> getAllEmployee();

	public ResponseEntity<?> createNewEmployee(SignupRequest signupRequest) throws NotFoundException;

}
