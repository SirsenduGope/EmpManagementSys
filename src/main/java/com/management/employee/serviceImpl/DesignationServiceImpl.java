package com.management.employee.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.management.employee.entity.Designation;
import com.management.employee.entity.EmployeeDetails;
import com.management.employee.payload.Message;
import com.management.employee.repository.DesignationRepository;
import com.management.employee.repository.EmployeeDetailsRepository;
import com.management.employee.service.IDesignationService;

import javassist.NotFoundException;

@Service
public class DesignationServiceImpl implements IDesignationService {
	
	private DesignationRepository designationRepo;
	private EmployeeDetailsRepository empDetailsRepo;
	
	public DesignationServiceImpl(final DesignationRepository designationRepo,
			final EmployeeDetailsRepository empDetailsRepo) {
		this.designationRepo = designationRepo;
		this.empDetailsRepo = empDetailsRepo;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(DesignationServiceImpl.class);
	
	
	@Override
	public ResponseEntity<?> getAllDesignation() throws Exception{
		List<Designation> designations = new ArrayList<Designation>();
		try {
			designations = designationRepo.findAll();
		}catch(Exception ex) {
			logger.debug("Error : exception from getAllDesignation.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<List<Designation>>(designations, HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> updateDesigntion(Designation designation) throws Exception{
		Designation updatedDesignation = new Designation();
		try {
			if(designation != null) {
				Optional<Designation> desg = designationRepo.findById(designation.getId());
				if(desg.isPresent()) {
					updatedDesignation = designationRepo.save(designation);
				}
				else {
					throw new EntityNotFoundException("No designation if found for id : " + designation.getId());
				}
			}
			else {
				throw new NotFoundException("No designation found for update");
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on fetching designation by id : " + designation.getId());
			throw new IllegalArgumentException("ERROR : Illegal argument exception on fetching designation by id : " + designation.getId());
		}catch(Exception ex) {
			logger.debug("Error : exception from updateDesigntion.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<Designation>(updatedDesignation, HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> addNewDesignation(Designation newDesignation) throws Exception{
		Designation designation = new Designation(); 
		try {
			if(newDesignation != null) {
				Boolean isExist = designationRepo.existsByDesignation(newDesignation.getDesignation());
				if(!isExist) {
					designation = designationRepo.save(newDesignation);
				}
				else {
					return new ResponseEntity<Message>(new Message("Designation is already exist."), HttpStatus.OK); 
				}
			}
			else {
				throw new NotFoundException("No designation found for save");
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on saving designation : " + newDesignation);
			throw new IllegalArgumentException("ERROR : Illegal argument exception on saving designation : " + newDesignation);
		}catch(Exception ex) {
			logger.debug("Error : exception from addNewDesignation.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<Designation>(designation, HttpStatus.CREATED);
	}
	
	
	@Override
	public ResponseEntity<?> deleteDesignation (String id) throws Exception{
		try {
			if(id != null) {
				Optional<Designation> designation = designationRepo.findById(Integer.parseInt(id));
				if(designation.isPresent()) {
					Optional<List<EmployeeDetails>> empDetails = empDetailsRepo.findByDesignationId(Integer.parseInt(id));
					if(empDetails.isPresent() && empDetails.get().size() > 0) {
						return new ResponseEntity<Message>(new Message("Unable to delete this designation as employees are holding this position."), HttpStatus.NOT_ACCEPTABLE);
					}
					else {
						designationRepo.delete(designation.get());
					}
				}
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on deleteDesignation for id : " + id);
			throw new IllegalArgumentException("ERROR : Illegal argument exception on for id : " + id);
		}catch(Exception ex) {
			logger.debug("Error : exception from deleteDesignation.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<Message>(new Message("Designation deleted Successfully."), HttpStatus.OK); 
	}


}
