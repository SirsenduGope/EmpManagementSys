package com.management.employee.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.employee.entity.Employee;
import com.management.employee.entity.EmployeeDetails;
import com.management.employee.entity.Views;
import com.management.employee.payload.Message;
import com.management.employee.repository.EmployeeRepository;


@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
	
	private EmployeeRepository empRepo;
	
	public EmployeeController(final EmployeeRepository empRepo) {
		this.empRepo = empRepo;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

//	@JsonView(Views.Public.class)
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public ResponseEntity<?> employeeRegister(@RequestBody Employee employee) {
//		Employee newEmployee = new Employee();
//		try {
//			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//			if(employee != null) {
//				if(employee.getEmail() != null && 
//						employee.getPassword() != null) {
//					
//					Employee isExistingEmp = empRepo.findByEmail(employee.getEmail());
//					if(isExistingEmp == null) {
//						String encodedPassword = passwordEncoder.encode(employee.getPassword());
//						employee.setPassword(encodedPassword);
//						newEmployee = empRepo.save(employee);
//					}
//					else {
//						Message msg = new Message("Duplicate entity found with email : " + employee.getEmail());
//						return new ResponseEntity<Message>(msg, HttpStatus.CONFLICT);
//					}
//					
//				}
//			}
//		}catch(IllegalArgumentException ex) {
//			logger.debug("Illigal argument exception on saving new employee, "+ex.getMessage());
//		}
//		catch(Exception ex) {
//			logger.debug("ERROR : Exception on new employee register : " + ex.getMessage());
//			logger.debug("Exception : " + ex.getStackTrace());
//		}
//		return new ResponseEntity<Employee>(newEmployee, HttpStatus.CREATED);
//	}
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@RequestMapping(value = "/saveemployeedetails", method = RequestMethod.POST)
	public ResponseEntity<?> saveEmployeeDetails(@RequestBody EmployeeDetails employeeDetails,
			@AuthenticationPrincipal Employee currentEmployee){
		Employee newEmployee = new Employee();
		if(employeeDetails != null && employeeDetails.getFirstName() != null) {
			String loginUserId = currentEmployee.getEmail();
			if(loginUserId != null) {
				try {
					Employee emp = empRepo.findByEmail(loginUserId);
					emp.setEmployeeDetails(employeeDetails);
					newEmployee = empRepo.save(emp);
				}catch(Exception ex) {
					logger.debug("ERROR : No Employye found for email : " + loginUserId);
					logger.debug("Stack Trace : " + ex.getStackTrace());
				}
			}
			else {
				return new ResponseEntity<Message>(new Message("Unable to get the current login user"), HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/allemployee", method = RequestMethod.GET)
	public ResponseEntity<?> getAllEmployee() {
		List<Employee> employees = new ArrayList<>();
		employees = empRepo.findAll();
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id){
		Employee emp = new Employee();
		try {
			if(id != null) {
				emp = empRepo.getById(Long.parseLong(id));
				if(emp == null) {
					return new ResponseEntity<Message>(new Message("Employee not found for Id : " + id), HttpStatus.NOT_FOUND);
				}
			}
		}catch(EntityNotFoundException ex) {
			logger.debug("ERROR : No Employye found for id : " + id);
			logger.debug("Stack Trace : " + ex.getStackTrace());
		}
		return new ResponseEntity<Employee>(emp, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") String id){
		try {
			if(id != null) {
				empRepo.deleteById(Long.parseLong(id));
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("Illigal argument exception on deleting employee with id : " + id + ", " +ex.getMessage());
		}
		catch(Exception ex) {
			logger.debug("ERROR : Exception on delete employee : " + ex.getMessage());
			logger.debug("Exception : " + ex.getStackTrace());
		}
		return new ResponseEntity<Message>(new Message("Employee deleted successfully with id : " + id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEmployee(@RequestBody Employee employee){
		Employee updatedEmployee = new Employee();
		try {
			if(employee.getId() != null && employee.getEmail() != null) {
				updatedEmployee = empRepo.save(employee);
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("Illigal argument exception on updating employee, "+ex.getMessage());
		}
		catch(Exception ex) {
			logger.debug("ERROR : Exception on updating employee : " + ex.getMessage());
			logger.debug("Exception : " + ex.getStackTrace());
		}
		
		return new ResponseEntity<Employee>(updatedEmployee, HttpStatus.OK);
	}
	
}
