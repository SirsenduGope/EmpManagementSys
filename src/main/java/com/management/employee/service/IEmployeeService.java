package com.management.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.management.employee.entity.Employee;
import com.management.employee.payload.EmployeeDetailsRequest;
import com.management.employee.payload.SignupRequest;

import javassist.NotFoundException;

public interface IEmployeeService {

	public ResponseEntity<?> getAllEmployee() throws Exception;

	public ResponseEntity<?> createNewEmployee(SignupRequest signupRequest) throws NotFoundException, Exception;

	public ResponseEntity<?> saveEmployeeDetails(EmployeeDetailsRequest employeeDetailsReq) throws NotFoundException, Exception;

	public ResponseEntity<?> getEmployeeById(String id) throws Exception;

	public ResponseEntity<?> deleteEmployeeById(String id) throws Exception;

	public ResponseEntity<?> getEmployeesByRole(String role) throws Exception;
	
	

	public Optional<List<Employee>> getAllEmployeesUnderLoggedInUser() throws Exception;

	public Optional<List<Long>> getAllEmployeesIdsUnderLoggedInUser() throws Exception;

	public Optional<List<Long>> getAllEmployeesIdUnderReportToUser(String reportToUserEmail) throws Exception;

	public Optional<Employee> getEmployeeById(Long id) throws Exception;

	public Optional<Employee> getEmployeeByEmail(String email) throws Exception;

	public List<Employee> getAllEmployeesByIds(List<Long> ids) throws Exception;

	public Optional<Employee> getLoggedInEmployeeDetails() throws Exception;

	public Optional<List<Employee>> getAllEmployeesUnderReportToUser(String reportToUserEmail) throws Exception;

	public boolean isLoggedInUserHasAccessToThisEmployee(Long id, boolean selfAccess) throws Exception;

}
