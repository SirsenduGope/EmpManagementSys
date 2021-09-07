package com.management.employee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.management.employee.entity.Designation;
import com.management.employee.service.IDesignationService;


@RestController
@RequestMapping(value = "/api/designation")
public class DesignationController {
	
	private IDesignationService designationService;
	
	public DesignationController(final IDesignationService designationService) {
		this.designationService = designationService;
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('MNAGER')")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> getAllDesignations() throws Exception{

		return designationService.getAllDesignation();
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveNewDesignation(@RequestBody Designation designation) throws Exception {
		
		return designationService.addNewDesignation(designation);
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateDesignation(@RequestBody Designation designation) throws Exception {
		
		return designationService.updateDesigntion(designation);
	}
	
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteDesignationById(@PathVariable("id") String id) throws Exception{

		return designationService.deleteDesignation(id);
	}
	
}
