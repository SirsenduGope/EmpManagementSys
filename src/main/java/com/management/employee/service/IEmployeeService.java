package com.management.employee.service;

import org.springframework.http.ResponseEntity;

import com.management.employee.payload.EmployeeDetailsRequest;
import com.management.employee.payload.SignupRequest;

import javassist.NotFoundException;

public interface IEmployeeService {

	public ResponseEntity<?> getAllEmployee();

	public ResponseEntity<?> createNewEmployee(SignupRequest signupRequest) throws NotFoundException, Exception;

	public ResponseEntity<?> saveEmployeeDetails(EmployeeDetailsRequest employeeDetailsReq) throws NotFoundException, Exception;

	public ResponseEntity<?> getEmployeeById(String id) throws Exception;

	public ResponseEntity<?> deleteEmployeeById(String id) throws Exception;

	public ResponseEntity<?> getEmployeesByRole(String role) throws Exception;

}
