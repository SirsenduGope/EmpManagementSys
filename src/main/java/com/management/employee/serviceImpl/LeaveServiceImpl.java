package com.management.employee.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.management.employee.entity.LeaveRecord;
import com.management.employee.entity.LeaveSettings;
import com.management.employee.enums.LeaveStatus;
import com.management.employee.payload.LeaveDetailsRequest;
import com.management.employee.payload.LeaveRequest;
import com.management.employee.payload.Message;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.repository.LeaveDetailsRepository;
import com.management.employee.repository.LeaveRecordRepository;
import com.management.employee.repository.LeaveSettingsRepository;
import com.management.employee.service.ILeaveService;

@Service
public class LeaveServiceImpl implements ILeaveService {
	
	private LeaveSettingsRepository leaveSettingsRepo;
	private LeaveDetailsRepository leaveDetailsRepo;
	private LeaveRecordRepository leaveRecordRepo;
	private EmployeeRepository empRepo;
	
	public LeaveServiceImpl(final LeaveSettingsRepository leaveSettingsRepo,
			final LeaveDetailsRepository leaveDetailsRepo,
			final LeaveRecordRepository leaveRecordRepo,
			final EmployeeRepository empRepo) {
		this.leaveSettingsRepo = leaveSettingsRepo;
		this.leaveDetailsRepo = leaveDetailsRepo;
		this.leaveRecordRepo = leaveRecordRepo;
		this.empRepo = empRepo;
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
				Double totalLeaves = 0d;
				Double remainingLeaves = 0d;
				
				if(!leaveSetting.isCasualLeaveMonthly()) {
					leaveDetails.setNoOfCasualLeave(leaveSetting.getCasualLeaveCount());
					leaveDetails.setRemainingCasualLeave(leaveSetting.getCasualLeaveCount());
					totalLeaves = totalLeaves + leaveSetting.getCasualLeaveCount();
					remainingLeaves = remainingLeaves + leaveSetting.getCasualLeaveCount();
				}
				if(!leaveSetting.isSickLeaveMonthly()) {
					leaveDetails.setNoOfSickLeave(leaveSetting.getSickLeaveCount());
					leaveDetails.setRemainingSickLeave(leaveSetting.getSickLeaveCount());
					totalLeaves = totalLeaves + leaveSetting.getSickLeaveCount();
					remainingLeaves = remainingLeaves + leaveSetting.getSickLeaveCount();
				}
				if(!leaveSetting.isEarnLeaveMonthly()) {
					leaveDetails.setNoOfEarnLeave(leaveSetting.getEarnLeaveCount());
					leaveDetails.setRemainingEarnLeave(leaveSetting.getEarnLeaveCount());
					totalLeaves = totalLeaves + leaveSetting.getEarnLeaveCount();
					remainingLeaves = remainingLeaves + leaveSetting.getEarnLeaveCount();
				}
				
				leaveDetails.setTotalLeaves(totalLeaves);
				leaveDetails.setRemainingLeaves(remainingLeaves);
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

	
	@Transactional
	@Override
	public ResponseEntity<?> requestForLeave(LeaveRequest leaveReq) throws Exception{
		LeaveRecord leaveRecord = null;
		int totalLeaveDays = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			if(leaveReq.getEmployeeId() != null) {
				Optional<Employee> emp = empRepo.findById(Long.parseLong(leaveReq.getEmployeeId()));
				if(emp.isPresent()) {
					Optional<LeaveDetails> leaveDetails = leaveDetailsRepo.findByEmployeeId(emp.get().getId());
					
					if(leaveDetails.isPresent()) {
						//Leave request FROM date must be less than TO date
						if(leaveReq.getLeaveRecord().getFromDate().before(leaveReq.getLeaveRecord().getToDate())) {
							try {
								Date fromDate = sdf.parse(leaveReq.getLeaveRecord().getFromDate().toString());
								Date toDate = sdf.parse(leaveReq.getLeaveRecord().getToDate().toString());
								totalLeaveDays = (int) ((Math.abs(toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24)) % 365);
							}catch (ParseException ex) {
								logger.debug("ERROR : Parse exception from requestForLeave method : " + ex.getMessage());
					            ex.printStackTrace();
					        }
							
							leaveRecord = new LeaveRecord(emp.get(), 
									leaveReq.getLeaveRecord().getFromDate(),
									leaveReq.getLeaveRecord().getToDate(),
									leaveReq.getLeaveRecord().getLeaveReason(),
									totalLeaveDays,
									LeaveStatus.REQUESTED,
									leaveReq.getLeaveRecord().getLeaveType(),
									new Date(), null);
							
							leaveRecordRepo.save(leaveRecord);
							
						}
						else {
							return new ResponseEntity<Message>(new Message("Leave FROM date must be less than TO date"), HttpStatus.BAD_REQUEST);
						}
					}
					else {
						return new ResponseEntity<Message>(new Message("No leave Details found for this employee. "
								+ "Please create leave details for employee : " + emp.get().getEmail()), HttpStatus.NOT_FOUND);
					}
				}
				else {
					return new ResponseEntity<Message>(new Message("No employee found for id : " + leaveReq.getEmployeeId()), HttpStatus.NOT_FOUND);
				}
				
			}
			else {
				return new ResponseEntity<Message>(new Message("Employee id is required to request for leave"), HttpStatus.BAD_REQUEST);
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : On save leave record for object : " + leaveRecord.toString());
			throw new IllegalArgumentException("ERROR : On save leave record for object : " + leaveRecord.toString());
		}catch(Exception ex) {
			logger.debug("ERROR : On save leave record for object : " + leaveRecord.toString());
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("On save leave record for object : " + leaveRecord.toString());
		}
		
		return new ResponseEntity<Message>(new Message("Leave record added successfully with object : " + leaveRecord.toString()), HttpStatus.CREATED);
	}
}
