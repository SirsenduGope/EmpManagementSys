package com.management.employee.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.management.employee.entity.LeaveRequestRecord;
import com.management.employee.entity.LeaveSettings;
import com.management.employee.enums.LeaveStatus;
import com.management.employee.enums.Roles;
import com.management.employee.payload.LeaveDetailsRequest;
import com.management.employee.payload.LeaveRequest;
import com.management.employee.payload.Message;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.repository.LeaveDetailsRepository;
import com.management.employee.repository.LeaveRequestRecordRepository;
import com.management.employee.repository.LeaveSettingsRepository;
import com.management.employee.service.ILeaveService;
import com.management.employee.utils.Helper;

@Service
public class LeaveServiceImpl implements ILeaveService {
	
	private LeaveSettingsRepository leaveSettingsRepo;
	private LeaveDetailsRepository leaveDetailsRepo;
	private LeaveRequestRecordRepository leaveRecordRepo;
	private EmployeeRepository empRepo;
	
	public LeaveServiceImpl(final LeaveSettingsRepository leaveSettingsRepo,
			final LeaveDetailsRepository leaveDetailsRepo,
			final LeaveRequestRecordRepository leaveRecordRepo,
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
		LeaveRequestRecord leaveRecord = null;
		int totalLeaveDays = 0;
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		Optional<Employee> emp = Optional.empty();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			if(leaveReq.getEmployeeId() != null) {
				emp = empRepo.findById(Long.parseLong(leaveReq.getEmployeeId()));
			}
			else {
				emp = empRepo.findByEmail(loggedInUSerEmail);
			}
			
			emp = empRepo.findById(Long.parseLong(leaveReq.getEmployeeId()));
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
						
						leaveRecord = new LeaveRequestRecord(emp.get(), 
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
	
	
	@Transactional
	@Override
	public ResponseEntity<?> updateLeaveRequest(LeaveRequest leaveRequest) throws Exception{
		LeaveRequestRecord leaveRecord = null;
		int totalLeaveDays = 0;
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		Optional<Employee> emp = Optional.empty();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			if(leaveRequest.getEmployeeId() != null) {
				emp = empRepo.findById(Long.parseLong(leaveRequest.getEmployeeId()));
			}
			else {
				emp = empRepo.findByEmail(loggedInUSerEmail);
			}
			
			Optional<LeaveRequestRecord> levRd = leaveRecordRepo.findByIdAndEmployeeId(leaveRequest.getLeaveRecord().getId(), emp.get().getId());
			if(levRd.isPresent()) {
				if(!levRd.get().getStatus().value.equals(LeaveStatus.ACCEPTED.value) ||
						!levRd.get().getStatus().value.equals(LeaveStatus.REJECTED.value)) {
					
					if(leaveRequest.getLeaveRecord().getFromDate().before(leaveRequest.getLeaveRecord().getToDate())) {
						try {
							Date fromDate = sdf.parse(leaveRequest.getLeaveRecord().getFromDate().toString());
							Date toDate = sdf.parse(leaveRequest.getLeaveRecord().getToDate().toString());
							totalLeaveDays = (int) ((Math.abs(toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24)) % 365);
						}catch (ParseException ex) {
							logger.debug("ERROR : Parse exception from requestForLeave method : " + ex.getMessage());
				            ex.printStackTrace();
				        }
						
						leaveRecord = levRd.get();
						leaveRecord.setFromDate(leaveRequest.getLeaveRecord().getFromDate());
						leaveRecord.setToDate(leaveRequest.getLeaveRecord().getToDate());
						leaveRecord.setLeaveReason(leaveRequest.getLeaveRecord().getLeaveReason());
						leaveRecord.setLeaveType(leaveRequest.getLeaveRecord().getLeaveType());
						leaveRecord.setTotalLeaveDays(totalLeaveDays);
						
						leaveRecordRepo.save(leaveRecord);
						
					}
					else {
						return new ResponseEntity<Message>(new Message("Leave FROM date must be less than TO date"), HttpStatus.BAD_REQUEST);
					}
					
				}
				else {
					return new ResponseEntity<Message>(new Message("This leave already either ACCEPTED or REJECTED. "
							+ "Can't edit this leave. Please apply a new leave if required"), HttpStatus.FORBIDDEN);
				}
			}
			else {
				return new ResponseEntity<Message>(new Message("No leave record found for id : " + leaveRequest.getLeaveRecord().getId()), HttpStatus.NOT_FOUND);
			}
			
		}catch(IllegalArgumentException ex) {
			logger.debug("ERROR : On update leave record for object : " + leaveRecord.toString());
			throw new IllegalArgumentException("ERROR : On update leave record for object : " + leaveRecord.toString());
		}catch(Exception ex) {
			logger.debug("ERROR : On update leave record for object : " + leaveRecord.toString());
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("On update leave record for object : " + leaveRecord.toString());
		}
		
		return new ResponseEntity<Message>(new Message("Leave record updated successfully with object : " + leaveRecord.toString()), HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> getAllRequetedLeaves(String leaveType, String leaveStatus) throws Exception{
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		Optional<List<LeaveRequestRecord>> leaveRecords = Optional.empty();
		
		try{
			//Get all the leave record for all employees under a manager
			if(authority.equals(Roles.ROLE_MANAGER.name())) {

				Optional<List<Long>> employeeIds = empRepo.findAllEmployeeIdByReportTo(loggedInUSerEmail);
				if(employeeIds.isPresent() && employeeIds.get().size() > 0) {
					if(leaveType == null && leaveStatus == null) {
						
						leaveRecords = leaveRecordRepo.findAllByEmployeeIdIn(employeeIds.get());
					}
					else {
						leaveRecords = getLeaveRecordByLeaveTypeAndLeaveStatus(leaveType, leaveStatus, employeeIds.get());
					}
					
					if(leaveRecords.isEmpty() || leaveRecords.get().size() == 0) {
						return new ResponseEntity<Message>(new Message("No leave record found"), HttpStatus.NOT_FOUND);
					}
				}
				else {
					return new ResponseEntity<Message>(new Message("No employee found under : " + loggedInUSerEmail), HttpStatus.NOT_FOUND);
				}
			}
			//Get all the leave record for all employees under a HR
			else if(authority.equals(Roles.ROLE_HR.name())) {
				List<Long> employeeIds = null;
				
				//Find Ids of all the managers
				Optional<List<Long>> managersIds = empRepo.findAllEmployeeIdByReportTo(loggedInUSerEmail);
				if(managersIds.isPresent() && managersIds.get().size() > 0) {
					employeeIds.addAll(managersIds.get());
					
					//Find all the managers under HR
					List<Employee> managers = empRepo.findAllById(managersIds.get());
					if(managers.size() > 0) {
						for(Employee manager : managers) {
							Optional<List<Long>> empIds = empRepo.findAllEmployeeIdByReportTo(manager.getEmail());
							if(empIds.isPresent() && empIds.get().size() > 0) {
								employeeIds.addAll(empIds.get());
							}
						}
						
						if(employeeIds.size() > 0) {
							if(leaveType == null && leaveStatus == null) {
								leaveRecords = leaveRecordRepo.findAllByEmployeeIdIn(employeeIds);
							}
							else {
								leaveRecords = getLeaveRecordByLeaveTypeAndLeaveStatus(leaveType, leaveStatus, employeeIds);
							}
							
							if(leaveRecords.isEmpty() || leaveRecords.get().size() == 0) {
								return new ResponseEntity<Message>(new Message("No leave record found"), HttpStatus.NOT_FOUND);
							}
						}
					}
					else {
						return new ResponseEntity<Message>(new Message("No employee found under : " + loggedInUSerEmail), HttpStatus.NOT_FOUND);
					}
				}
				else {
					return new ResponseEntity<Message>(new Message("No employee found under : " + loggedInUSerEmail), HttpStatus.NOT_FOUND);
				}
			}
			//Get all the leave record for all employees 
			else {
				if(leaveType == null && leaveStatus == null) {
					List<LeaveRequestRecord> leaveRecordOfAllEmp = leaveRecordRepo.findAll();
					if(leaveRecordOfAllEmp.size() > 0) {
						leaveRecords = Optional.of(leaveRecordOfAllEmp);
					}
				}
				else {
					leaveRecords = getLeaveRecordByLeaveTypeAndLeaveStatus(leaveType, leaveStatus, null);
				}
				
				if(leaveRecords.isEmpty() || leaveRecords.get().size() == 0) {
					return new ResponseEntity<Message>(new Message("No leave record found"), HttpStatus.NOT_FOUND);
				}
				
			}
		}catch(Exception ex) {
			logger.debug("ERROR : On fetch leave record from method getAllRequetedLeaves");
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On fetch leave record from method getAllRequetedLeaves", ex);
		}
		
		
		return new ResponseEntity<List<LeaveRequestRecord>>(leaveRecords.get(), HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> getAllMyLeaves(String leaveType, String leaveStatus) throws Exception{
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		Optional<List<LeaveRequestRecord>> leaveRecords = Optional.empty();
		
		try {
			Optional<Employee> employee = empRepo.findByEmail(loggedInUSerEmail);
			if(employee.isPresent()) {
				List<Long> empIds = new ArrayList<Long>();
				empIds.add(employee.get().getId());
				
				if(leaveType == null && leaveStatus == null) {
					leaveRecords = leaveRecordRepo.findAllByEmployeeIdIn(empIds);
				}
				else {
					leaveRecords = getLeaveRecordByLeaveTypeAndLeaveStatus(leaveType, leaveStatus, empIds);
				}
				
				if(leaveRecords.isEmpty() || leaveRecords.get().size() == 0) {
					return new ResponseEntity<Message>(new Message("No leave record found"), HttpStatus.NOT_FOUND);
				}
			}
		}catch(Exception ex) {
			logger.debug("ERROR : On fetch leave record from method getAllMyLeaves");
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On fetch leave record from method getAllMyLeaves", ex);
		}
		
		
		return new ResponseEntity<List<LeaveRequestRecord>>(leaveRecords.get(), HttpStatus.OK);
		
	}

	
	private Optional<List<LeaveRequestRecord>> getLeaveRecordByLeaveTypeAndLeaveStatus(String leaveType, String leaveStatus, List<Long> empIds) throws Exception{
		Optional<List<LeaveRequestRecord>> leaveRecords = Optional.empty();
		
		try {
			if(leaveType != null && leaveStatus == null) {
				if(empIds != null) {
					leaveRecords = leaveRecordRepo.findByLeaveTypeAndEmployeeIdIn(leaveType, empIds);
				}
				else {
					leaveRecords = leaveRecordRepo.findByLeaveType(leaveType);
				}
				
			}
			else if (leaveType == null && leaveStatus != null) {
				if(empIds != null) {
					leaveRecords = leaveRecordRepo.findByLeaveStatusAndEmployeeIdIn(leaveStatus, empIds);
				}
				else {
					leaveRecords = leaveRecordRepo.findByLeaveStatus(leaveStatus);
				}
				
			}
			else if(leaveType != null && leaveStatus != null) {
				if(empIds != null) {
					leaveRecords = leaveRecordRepo.findByLeaveTypeAndLeaveStatusAndEmployeeIdIn(leaveType, leaveStatus, empIds);
				}
				else {
					leaveRecords = leaveRecordRepo.findByLeaveTypeAndLeaveStatus(leaveType, leaveStatus);
				}
				
			}
		}catch(Exception ex) {
			logger.debug("ERROR : On fetch leave record from method getLeaveRecordByLeaveTypeAndLeaveStatus");
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On fetch leave record from method getLeaveRecordByLeaveTypeAndLeaveStatus", ex);
		}
		
		return leaveRecords;
	}
}
