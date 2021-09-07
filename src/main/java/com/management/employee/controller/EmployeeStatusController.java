package com.management.employee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.management.employee.entity.EmployeeStatus;
import com.management.employee.service.IEmployeeStatusService;


@RestController
@RequestMapping(value = "/api/empstatus")
public class EmployeeStatusController {
	
	private IEmployeeStatusService empStatusService;
	
	public EmployeeStatusController(final IEmployeeStatusService empStatusService) {
		this.empStatusService = empStatusService;
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('MNAGER')")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> getAllEmployeeStatus() throws Exception{

		return empStatusService.getAllStatus();
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveNewEmployeeStatus(@RequestBody EmployeeStatus empStatus) throws Exception {
		
		return empStatusService.addNewEmployeeStauts(empStatus);
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEmployeeStatus(@RequestBody EmployeeStatus empStatus) throws Exception {
		
		return empStatusService.updateEmployeeStatus(empStatus);
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEmployeeStatusById(@PathVariable("id") String id) throws Exception{

		return empStatusService.deleteEmployeeStatusById(id);
	}
	
}
