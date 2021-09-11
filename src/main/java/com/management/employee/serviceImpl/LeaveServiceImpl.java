package com.management.employee.serviceImpl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.management.employee.entity.Employee;
import com.management.employee.entity.LeaveDetails;
import com.management.employee.entity.LeaveSettings;
import com.management.employee.payload.LeaveDetailsRequest;
import com.management.employee.payload.Message;
import com.management.employee.repository.LeaveDetailsRepository;
import com.management.employee.repository.LeaveSettingsRepository;
import com.management.employee.service.ILeaveService;

@Service
public class LeaveServiceImpl implements ILeaveService {
	
	private LeaveSettingsRepository leaveSettingsRepo;
	private LeaveDetailsRepository leaveDetailsRepo;
	
	public LeaveServiceImpl(final LeaveSettingsRepository leaveSettingsRepo,
			final LeaveDetailsRepository leaveDetailsRepo) {
		this.leaveSettingsRepo = leaveSettingsRepo;
		this.leaveDetailsRepo = leaveDetailsRepo;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(LeaveServiceImpl.class);
	
	@Transactional
	@Override
	public ResponseEntity<?> configureLeaveSettings(LeaveSettings leaveSettings) throws Exception{
		try {
			leaveSettingsRepo.save(leaveSettings);
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on saving leaveSettings : " + leaveSettings.toString());
			throw new IllegalArgumentException("ERROR : Illegal argument exception on saving leaveSettings :" + leaveSettings.toString());
		}catch(Exception ex) {
			logger.debug("Error : exception from configureLeaveSettings.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<Message>(new Message("Leave settings saved successfully"), HttpStatus.CREATED);
		
	}
	
	
	@Transactional
	@Override
	public ResponseEntity<?> generateLeaveDetailsForEmployee(Employee employee) throws Exception{
		LeaveDetails leaveDetails = new LeaveDetails();
		try {
			List<LeaveSettings> leaveSettings = leaveSettingsRepo.findAll();
			if(leaveSettings.size() > 0) {
				LeaveSettings leaveSetting = leaveSettings.get(0);
				
				if(!leaveSetting.isCasualLeaveMonthly()) {
					leaveDetails.setNoOfCasualLeave(leaveSetting.getCasualLeaveCount());
					leaveDetails.setRemainingCasualLeave(leaveSetting.getCasualLeaveCount());
				}
				if(!leaveSetting.isSickLeaveMonthly()) {
					leaveDetails.setNoOfSickLeave(leaveSetting.getSickLeaveCount());
					leaveDetails.setRemainingSickLeave(leaveSetting.getSickLeaveCount());
				}
				if(!leaveSetting.isEarnLeaveMonthly()) {
					leaveDetails.setNoOfEarnLeave(leaveSetting.getEarnLeaveCount());
					leaveDetails.setRemainingEarnLeave(leaveSetting.getEarnLeaveCount());
				}
				
				leaveDetails.setEmployee(employee);
				leaveDetailsRepo.save(leaveDetails);
				
			}
			else {
				return new ResponseEntity<Message>(new Message("There is no Leave Settings still configured."
						+ " Please contact with Admin to configure Leaev Settings before create any new user"), HttpStatus.FORBIDDEN);
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : Illegal argument exception on saving leave details : " + leaveDetails.toString());
			throw new IllegalArgumentException("ERROR : Illegal argument exception on saving leave details : " + leaveDetails.toString());
		}catch(Exception ex) {
			logger.debug("Error : exception from generateLeaveDetailsForEmployee.");
			throw new Exception(ex);
		}
		
		return new ResponseEntity<Message>(new Message("Leave details saved successfullt for employee : " + employee.getEmail()), HttpStatus.CREATED); 
	}
	

	@Transactional
	@Override
	public ResponseEntity<?> updateLeaveDetailsForEmployee(LeaveDetailsRequest leaveDetailsReq) throws Exception{
		LeaveDetails leaveDetails = null;
		try {
			if(leaveDetailsReq.getEmployeeId() != null) {
				Optional<LeaveDetails> leaveDtl = leaveDetailsRepo.findByEmployeeId(Long.parseLong(leaveDetailsReq.getEmployeeId()));
				if(leaveDtl.isPresent()) {
					leaveDetails = leaveDtl.get();
					leaveDetails.setNoOfCasualLeave(leaveDetailsReq.getLeaveDetails().getNoOfCasualLeave());
					leaveDetails.setNoOfSickLeave(leaveDetailsReq.getLeaveDetails().getNoOfSickLeave());
					leaveDetails.setNoOfEarnLeave(leaveDetailsReq.getLeaveDetails().getNoOfEarnLeave());
					
					leaveDetails.setRemainingCasualLeave(leaveDetailsReq.getLeaveDetails().getRemainingCasualLeave());
					leaveDetails.setRemainingSickLeave(leaveDetailsReq.getLeaveDetails().getRemainingSickLeave());
					leaveDetails.setRemainingEarnLeave(leaveDetailsReq.getLeaveDetails().getRemainingEarnLeave());
					
					leaveDetails.setTotalLOP(leaveDetailsReq.getLeaveDetails().getTotalLOP());
					
					leaveDetailsRepo.save(leaveDetails);
				}
				else {
					return new ResponseEntity<Message>(new Message("No leave Details found for this employee. "
							+ "Please create leave details for employee : " + leaveDetailsReq.getEmployeeId()), HttpStatus.NOT_FOUND);
				}
			}
			else {
				return new ResponseEntity<Message>(new Message("Employee id is required to update leave details"), HttpStatus.BAD_REQUEST);
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : On update leave details for object : " + leaveDetails.toString());
			throw new IllegalArgumentException("ERROR : On update leave details for object : " + leaveDetails.toString());
		}catch(Exception ex) {
			logger.debug("ERROR : On update leave details for employee id : " + leaveDetailsReq.getEmployeeId());
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On update leave details for employee id : " + leaveDetailsReq.getEmployeeId());
		}
		
		return new ResponseEntity<Message>(new Message("Leave details update successfully for emaployee id : " + leaveDetailsReq.getEmployeeId()), HttpStatus.OK);
	}

}
