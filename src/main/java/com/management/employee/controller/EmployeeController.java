package com.management.employee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.management.employee.payload.EmployeeDetailsRequest;
import com.management.employee.payload.SignupRequest;
import com.management.employee.service.IEmployeeService;


@RestController
@RequestMapping(value = "/api/employee")
public class EmployeeController {
	
	private IEmployeeService employeeService;
	
	public EmployeeController(final IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<?> saveEmployeeDetails(@RequestBody EmployeeDetailsRequest employeeDetailsReq) throws Exception{

		return employeeService.saveEmployeeDetails(employeeDetailsReq);
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> createNewEmployee(@RequestBody SignupRequest signupRequest) throws Exception {
		
		return employeeService.createNewEmployee(signupRequest);
	}
	
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> getAllEmployee() throws Exception {
		
		return employeeService.getAllEmployee();
	}
	
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id) throws Exception{

		return employeeService.getEmployeeById(id);
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") String id) throws Exception{

		return employeeService.deleteEmployeeById(id);
	}
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/list/{role}", method = RequestMethod.GET)
	public ResponseEntity<?> getEmployeesByRole(@PathVariable("role") String role) throws Exception{

		return employeeService.getEmployeesByRole(role);
	}

}
