package com.management.employee.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.management.employee.entity.Designation;

public interface IDesignationService {

	public ResponseEntity<?> deleteDesignation(String id) throws Exception;

	public ResponseEntity<?> addNewDesignation(Designation designation) throws Exception;

	public ResponseEntity<?> updateDesigntion(Designation designation) throws Exception;

	public ResponseEntity<?> getAllDesignation() throws Exception;

	public Optional<Designation> getDesignationDetailsByName(String designation) throws Exception;


}
