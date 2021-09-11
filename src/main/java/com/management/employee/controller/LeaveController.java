package com.management.employee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.management.employee.entity.LeaveSettings;
import com.management.employee.payload.LeaveDetailsRequest;
import com.management.employee.service.ILeaveService;


@RestController
@RequestMapping(value = "/api/leave")
public class LeaveController {
	
	private ILeaveService leaveService;
	
	public LeaveController(final ILeaveService leaveService) {
		this.leaveService = leaveService;
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public ResponseEntity<?> leaveSettings(@RequestBody LeaveSettings leaveSettings) throws Exception{

		return leaveService.configureLeaveSettings(leaveSettings);
	}
	
	@PreAuthorize("hasRole('ADMIN') || hasRole('HR')")
	@RequestMapping(value = "/details", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLeaveDetailsByEmployeeId(@RequestBody LeaveDetailsRequest leaveDetailsReq) throws Exception{

		return leaveService.updateLeaveDetailsForEmployee(leaveDetailsReq);
	}
	
	
//	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
//	@RequestMapping(value = "/save", method = RequestMethod.POST)
//	public ResponseEntity<?> saveNewEmployeeStatus(@RequestBody EmployeeStatus empStatus) throws Exception {
//		
//		return empStatusService.addNewEmployeeStauts(empStatus);
//	}
//	
//	
//	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
//	@RequestMapping(value = "/update", method = RequestMethod.PUT)
//	public ResponseEntity<?> updateEmployeeStatus(@RequestBody EmployeeStatus empStatus) throws Exception {
//		
//		return empStatusService.updateEmployeeStatus(empStatus);
//	}
//	
//	
//	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
//	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
//	public ResponseEntity<?> deleteEmployeeStatusById(@PathVariable("id") String id) throws Exception{
//
//		return empStatusService.deleteEmployeeStatusById(id);
//	}
//	
}
