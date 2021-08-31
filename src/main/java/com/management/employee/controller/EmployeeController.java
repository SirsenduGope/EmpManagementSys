package com.management.employee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.employee.entity.Views;
import com.management.employee.payload.EmployeeDetailsRequest;
import com.management.employee.payload.SignupRequest;
import com.management.employee.service.IEmployeeService;

import javassist.NotFoundException;


@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
	
	private IEmployeeService employeeService;
	
	public EmployeeController(final IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/saveemployeedetails", method = RequestMethod.POST)
	public ResponseEntity<?> saveEmployeeDetails(@RequestBody EmployeeDetailsRequest employeeDetailsReq) throws NotFoundException{

		return employeeService.saveEmployeeDetails(employeeDetailsReq);
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> createNewEmployee(@RequestBody SignupRequest signupRequest) throws NotFoundException {
		
		return employeeService.createNewEmployee(signupRequest);
	}
	
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/allemployee", method = RequestMethod.GET)
	public ResponseEntity<?> getAllEmployee() {
		
		return employeeService.getAllEmployee();
	}
	
//	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
//	@JsonView(Views.Public.class)
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
//	public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id){
//		Employee emp = new Employee();
//		try {
//			if(id != null) {
//				emp = empRepo.getById(Long.parseLong(id));
//				if(emp == null) {
//					return new ResponseEntity<Message>(new Message("Employee not found for Id : " + id), HttpStatus.NOT_FOUND);
//				}
//			}
//		}catch(EntityNotFoundException ex) {
//			logger.debug("ERROR : No Employye found for id : " + id);
//			logger.debug("Stack Trace : " + ex.getStackTrace());
//		}
//		return new ResponseEntity<Employee>(emp, HttpStatus.OK);
//	}
//	
//	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
//	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
//	public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") String id){
//		try {
//			if(id != null) {
//				empRepo.deleteById(Long.parseLong(id));
//			}
//		}catch(IllegalArgumentException ex) {
//			logger.debug("Illigal argument exception on deleting employee with id : " + id + ", " +ex.getMessage());
//		}
//		catch(Exception ex) {
//			logger.debug("ERROR : Exception on delete employee : " + ex.getMessage());
//			logger.debug("Exception : " + ex.getStackTrace());
//		}
//		return new ResponseEntity<Message>(new Message("Employee deleted successfully with id : " + id), HttpStatus.OK);
//	}
//	
//	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
//	@JsonView(Views.Public.class)
//	@RequestMapping(value = "/update", method = RequestMethod.PUT)
//	public ResponseEntity<?> updateEmployee(@RequestBody Employee employee){
//		Employee updatedEmployee = new Employee();
//		try {
//			if(employee.getId() != null && employee.getEmail() != null) {
//				updatedEmployee = empRepo.save(employee);
//			}
//		}catch(IllegalArgumentException ex) {
//			logger.debug("Illigal argument exception on updating employee, "+ex.getMessage());
//		}
//		catch(Exception ex) {
//			logger.debug("ERROR : Exception on updating employee : " + ex.getMessage());
//			logger.debug("Exception : " + ex.getStackTrace());
//		}
//		
//		return new ResponseEntity<Employee>(updatedEmployee, HttpStatus.OK);
//	}
	
}
