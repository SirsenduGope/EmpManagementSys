package com.management.employee.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.management.employee.entity.EmployeeDetails;
import com.management.employee.entity.EmployeeStatus;
import com.management.employee.payload.Message;
import com.management.employee.repository.EmployeeDetailsRepository;
import com.management.employee.repository.EmployeeStatusRepository;
import com.management.employee.service.IEmployeeStatusService;

import javassist.NotFoundException;

@Service
public class EmployeeStatusServiceImpl implements IEmployeeStatusService {
	
	private EmployeeStatusRepository empStatusRepo;
	private EmployeeDetailsRepository empDetailsRepo;
	
	public EmployeeStatusServiceImpl(final EmployeeStatusRepository empStatusRepo,
			final EmployeeDetailsRepository empDetailsRepo) {
		this.empStatusRepo = empStatusRepo;
		this.empDetailsRepo = empDetailsRepo;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeStatusServiceImpl.class);
	
	
	@Override
	public ResponseEntity<?> getAllStatus() throws Exception{
		List<EmployeeStatus> empStatus = new ArrayList<EmployeeStatus>();
		try {
			empStatus = empStatusRepo.findAll();
		}catch(Exception ex) {
			logger.debug("Error : exception from getAllStatus.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<List<EmployeeStatus>>(empStatus, HttpStatus.OK);
	}
	
	
	@Transactional
	@Override
	public ResponseEntity<?> updateEmployeeStatus(EmployeeStatus employeeStatus) throws Exception{
		EmployeeStatus updatedEmpStatus = new EmployeeStatus();
		try {
			if(employeeStatus != null) {
				Optional<EmployeeStatus> empStatus = empStatusRepo.findById(employeeStatus.getId());
				if(empStatus.isPresent()) {
					updatedEmpStatus = empStatusRepo.save(employeeStatus);
				}
				else {
					throw new EntityNotFoundException("No employee Status if found for id : " + employeeStatus.getId());
				}
			}
			else {
				throw new NotFoundException("No employee status found for update");
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on fetching employee status by id : " + employeeStatus.getId());
			throw new IllegalArgumentException("ERROR : Illegal argument exception on fetching employee status by id : " + employeeStatus.getId());
		}catch(Exception ex) {
			logger.debug("Error : exception from updateEmployeeStatus.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<EmployeeStatus>(updatedEmpStatus, HttpStatus.OK);
	}
	
	
	@Transactional
	@Override
	public ResponseEntity<?> addNewEmployeeStauts(EmployeeStatus newEmployeeStatus) throws Exception{
		EmployeeStatus empStatus = new EmployeeStatus(); 
		try {
			if(newEmployeeStatus != null) {
				Boolean isExist = empStatusRepo.existsByStatus(newEmployeeStatus.getStatus());
				if(!isExist) {
					empStatus = empStatusRepo.save(newEmployeeStatus);
				}
				else {
					return new ResponseEntity<Message>(new Message("Employee Status is already exist."), HttpStatus.OK); 
				}
			}
			else {
				throw new NotFoundException("No employee status found for save");
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on saving employee status : " + newEmployeeStatus);
			throw new IllegalArgumentException("ERROR : Illegal argument exception on saving employee status : " + newEmployeeStatus);
		}catch(Exception ex) {
			logger.debug("Error : exception from addNewEmployeeStauts.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<EmployeeStatus>(empStatus, HttpStatus.CREATED);
	}
	
	
	@Transactional
	@Override
	public ResponseEntity<?> deleteEmployeeStatusById (String id) throws Exception{
		try {
			if(id != null) {
				Optional<EmployeeStatus> empStatus = empStatusRepo.findById(Integer.parseInt(id));
				if(empStatus.isPresent()) {
					Optional<List<EmployeeDetails>> empDetails = empDetailsRepo.findByEmpStatusId(Integer.parseInt(id));
					if(empDetails.isPresent() && empDetails.get().size() > 0) {
						return new ResponseEntity<Message>(new Message("Unable to delete this employee status as employees are holding this status."), HttpStatus.NOT_ACCEPTABLE);
					}
					else {
						empStatusRepo.delete(empStatus.get());
					}
				}
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on deleteEmployeeStatusById for id : " + id);
			throw new IllegalArgumentException("ERROR : Illegal argument exception on deleteEmployeeStatusById for id : " + id);
		}catch(Exception ex) {
			logger.debug("Error : exception from deleteEmployeeStatusById.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<Message>(new Message("Employee status deleted Successfully."), HttpStatus.OK); 
	}

	
	@Override
	public Optional<EmployeeStatus> getEmployeeStatusByName(String empStatus) throws Exception{
		try {
			return empStatusRepo.findByStatus(empStatus);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getDesignationDetailsByName method.");
			throw new Exception(ex);
		}
	}

}
