package com.management.employee.serviceImpl;

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
import com.management.employee.entity.LeaveCountDetails;
import com.management.employee.entity.LeaveRequestRecord;
import com.management.employee.entity.LeaveSettings;
import com.management.employee.enums.LeaveStatus;
import com.management.employee.enums.LeaveType;
import com.management.employee.enums.Roles;
import com.management.employee.payload.LeaveDetailsRequest;
import com.management.employee.payload.Message;
import com.management.employee.repository.LeaveCountDetailsRepository;
import com.management.employee.repository.LeaveRequestRecordRepository;
import com.management.employee.repository.LeaveSettingsRepository;
import com.management.employee.service.IEmployeeService;
import com.management.employee.service.ILeaveService;
import com.management.employee.utils.Helper;

@Service
public class LeaveServiceImpl implements ILeaveService {
	
	private LeaveSettingsRepository leaveSettingsRepo;
	private LeaveCountDetailsRepository leaveCountDetailsRepo;
	private LeaveRequestRecordRepository leaveRequestRecordRepo;
	
	private IEmployeeService employeeService;
	
	public LeaveServiceImpl(final LeaveSettingsRepository leaveSettingsRepo,
			final LeaveCountDetailsRepository leaveCountDetailsRepo,
			final LeaveRequestRecordRepository leaveRequestRecordRepo,
			final IEmployeeService employeeService) {
		this.leaveSettingsRepo = leaveSettingsRepo;
		this.leaveCountDetailsRepo = leaveCountDetailsRepo;
		this.leaveRequestRecordRepo = leaveRequestRecordRepo;
		this.employeeService = employeeService;
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
	public ResponseEntity<?> generateLeaveCountDetailsForEmployee(Employee employee) throws Exception{
		LeaveCountDetails leaveDetails = new LeaveCountDetails();
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
				leaveCountDetailsRepo.save(leaveDetails);
				
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
	public ResponseEntity<?> updateLeaveCountDetailsForEmployee(LeaveDetailsRequest leaveDetailsReq) throws Exception{
		LeaveCountDetails leaveCountDetails = null;
		try {
			if(leaveDetailsReq.getEmployeeId() != null) {
				Optional<LeaveCountDetails> leaveDtl = leaveCountDetailsRepo.findByEmployeeId(Long.parseLong(leaveDetailsReq.getEmployeeId()));
				if(leaveDtl.isPresent()) {
					leaveCountDetails = leaveDtl.get();
					leaveCountDetails.setNoOfCasualLeave(leaveDetailsReq.getLeaveDetails().getNoOfCasualLeave());
					leaveCountDetails.setNoOfSickLeave(leaveDetailsReq.getLeaveDetails().getNoOfSickLeave());
					leaveCountDetails.setNoOfEarnLeave(leaveDetailsReq.getLeaveDetails().getNoOfEarnLeave());
					
					leaveCountDetails.setRemainingCasualLeave(leaveDetailsReq.getLeaveDetails().getRemainingCasualLeave());
					leaveCountDetails.setRemainingSickLeave(leaveDetailsReq.getLeaveDetails().getRemainingSickLeave());
					leaveCountDetails.setRemainingEarnLeave(leaveDetailsReq.getLeaveDetails().getRemainingEarnLeave());
					
					leaveCountDetails.setTotalLOP(leaveDetailsReq.getLeaveDetails().getTotalLOP());
					
					leaveCountDetailsRepo.save(leaveCountDetails);
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
			logger.debug("ERROR : On update leave details for object : " + leaveCountDetails.toString());
			throw new IllegalArgumentException("ERROR : On update leave details for object : " + leaveCountDetails.toString());
		}catch(Exception ex) {
			logger.debug("ERROR : On update leave details for employee id : " + leaveDetailsReq.getEmployeeId());
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On update leave details for employee id : " + leaveDetailsReq.getEmployeeId());
		}
		
		return new ResponseEntity<Message>(new Message("Leave details update successfully for emaployee id : " + leaveDetailsReq.getEmployeeId()), HttpStatus.OK);
	}

	
	@Transactional
	@Override
	public ResponseEntity<?> requestForLeave(LeaveRequestRecord leaveReq) throws Exception{
		LeaveRequestRecord leaveRecord = null;
		int totalLeaveDays = 0;
		Optional<Employee> emp = Optional.empty();
		
		try {
			emp = employeeService.getLoggedInEmployeeDetails();
			
			if(emp.isPresent()) {
				Optional<LeaveCountDetails> leaveCountDetails = leaveCountDetailsRepo.findByEmployeeId(emp.get().getId());
				
				if(leaveCountDetails.isPresent()) {
					//Leave request FROM date must be less than TO date
					if(leaveReq.getFromDate().before(leaveReq.getToDate())) {
						totalLeaveDays = Helper.getTotalLeavesByCalculatingFromDateToDate(
								leaveReq.getFromDate(), 
								leaveReq.getToDate());
						
						leaveRecord = new LeaveRequestRecord(emp.get(), 
								leaveReq.getFromDate(),
								leaveReq.getToDate(),
								leaveReq.getLeaveReason(),
								totalLeaveDays,
								LeaveStatus.REQUESTED,
								leaveReq.getLeaveType(),
								new Date(), null, null);
						
						leaveRequestRecordRepo.save(leaveRecord);
						
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
				return new ResponseEntity<Message>(new Message("No employee found for id : " + emp.get().getId()), HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> updateLeaveRequestRecord(LeaveRequestRecord leaveRequest) throws Exception{
		LeaveRequestRecord leaveRecord = null;
		int totalLeaveDays = 0;
		Optional<Employee> emp = Optional.empty();
		
		try {
			emp = employeeService.getLoggedInEmployeeDetails();
			
			Optional<LeaveRequestRecord> levRd = leaveRequestRecordRepo.findByIdAndEmployeeId(leaveRequest.getId(), emp.get().getId());
			if(levRd.isPresent()) {
				if(!levRd.get().getStatus().value.equals(LeaveStatus.ACCEPTED.value) ||
						!levRd.get().getStatus().value.equals(LeaveStatus.REJECTED.value)) {
					
					if(leaveRequest.getFromDate().before(leaveRequest.getToDate())) {
						totalLeaveDays = Helper.getTotalLeavesByCalculatingFromDateToDate(
								leaveRequest.getFromDate(),
								leaveRequest.getToDate());
						
						leaveRecord = levRd.get();
						leaveRecord.setFromDate(leaveRequest.getFromDate());
						leaveRecord.setToDate(leaveRequest.getToDate());
						leaveRecord.setLeaveReason(leaveRequest.getLeaveReason());
						leaveRecord.setLeaveType(leaveRequest.getLeaveType());
						leaveRecord.setTotalLeaveDays(totalLeaveDays);
						
						leaveRequestRecordRepo.save(leaveRecord);
						
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
				return new ResponseEntity<Message>(new Message("No leave record found for id : " + leaveRequest.getId()), HttpStatus.NOT_FOUND);
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

				Optional<List<Long>> employeeIds = employeeService.getAllEmployeesIdsUnderLoggedInUser();
				if(employeeIds.isPresent() && employeeIds.get().size() > 0) {
					if(leaveType == null && leaveStatus == null) {
						
						leaveRecords = leaveRequestRecordRepo.findAllByEmployeeIdIn(employeeIds.get());
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
				List<Long> employeeIds = new ArrayList<Long>();
				
				//Find Id's of all the managers
				Optional<List<Long>> managersIds = employeeService.getAllEmployeesIdsUnderLoggedInUser();
				if(managersIds.isPresent() && managersIds.get().size() > 0) {
					employeeIds.addAll(managersIds.get());
					
					//Find all the managers under HR
					List<Employee> managers = employeeService.getAllEmployeesByIds(managersIds.get());
					if(managers.size() > 0) {
						for(Employee manager : managers) {
							Optional<List<Long>> empIds = employeeService.getAllEmployeesIdUnderReportToUser(manager.getEmail());
							if(empIds.isPresent() && empIds.get().size() > 0) {
								employeeIds.addAll(empIds.get());
							}
						}
						
						if(employeeIds.size() > 0) {
							if(leaveType == null && leaveStatus == null) {
								leaveRecords = leaveRequestRecordRepo.findAllByEmployeeIdIn(employeeIds);
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
					List<LeaveRequestRecord> leaveRecordOfAllEmp = leaveRequestRecordRepo.findAll();
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
		Optional<List<LeaveRequestRecord>> leaveRecords = Optional.empty();
		
		try {
			Optional<Employee> employee = employeeService.getLoggedInEmployeeDetails();
			if(employee.isPresent()) {
				List<Long> empIds = new ArrayList<Long>();
				empIds.add(employee.get().getId());
				
				if(leaveType == null && leaveStatus == null) {
					leaveRecords = leaveRequestRecordRepo.findAllByEmployeeIdIn(empIds);
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
					leaveRecords = leaveRequestRecordRepo.findByLeaveTypeAndEmployeeIdIn(leaveType, empIds);
				}
				else {
					leaveRecords = leaveRequestRecordRepo.findByLeaveType(leaveType);
				}
				
			}
			else if (leaveType == null && leaveStatus != null) {
				if(empIds != null) {
					leaveRecords = leaveRequestRecordRepo.findByLeaveStatusAndEmployeeIdIn(leaveStatus, empIds);
				}
				else {
					leaveRecords = leaveRequestRecordRepo.findByLeaveStatus(leaveStatus);
				}
				
			}
			else if(leaveType != null && leaveStatus != null) {
				if(empIds != null) {
					leaveRecords = leaveRequestRecordRepo.findByLeaveTypeAndLeaveStatusAndEmployeeIdIn(leaveType, leaveStatus, empIds);
				}
				else {
					leaveRecords = leaveRequestRecordRepo.findByLeaveTypeAndLeaveStatus(leaveType, leaveStatus);
				}
				
			}
		}catch(Exception ex) {
			logger.debug("ERROR : On fetch leave record from method getLeaveRecordByLeaveTypeAndLeaveStatus");
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On fetch leave record from method getLeaveRecordByLeaveTypeAndLeaveStatus", ex);
		}
		
		return leaveRecords;
	}
	
	
	@Override
	public ResponseEntity<?> getLeaveRequestRecordById(String id) throws Exception{
		Optional<LeaveRequestRecord> leaveReqRecord = Optional.empty();
		boolean hasAccess = false;
		
		try {
			leaveReqRecord = leaveRequestRecordRepo.findById(Integer.parseInt(id));
			if(leaveReqRecord.isPresent()) {
				Long leaveRequestedEmployeeId = leaveReqRecord.get().getEmployee().getId();
				hasAccess = employeeService.isLoggedInUserHasAccessToThisEmployee(leaveRequestedEmployeeId, true);
				if(!hasAccess) {
					return new ResponseEntity<Message>(new Message("You don't have permission to view this user's leave request details."), HttpStatus.FORBIDDEN);
				}
			}
			else {
				return new ResponseEntity<Message>(new Message("No Leave request record found for id : " + id), HttpStatus.NOT_FOUND);
			}
			
		}catch(Exception ex) {
			logger.debug("ERROR : On fetch leave request record from method getLeaveRequestRecordById");
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On fetch leave request record from method getLeaveRequestRecordById", ex);
		}
		
		return new ResponseEntity<LeaveRequestRecord>(leaveReqRecord.get(), HttpStatus.OK);
	}
	
	
	@Transactional
	@Override
	public ResponseEntity<?> leaveApproveOrRejectAction(String action, LeaveRequestRecord leaveReq) throws Exception{
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		boolean hasAccess = false;
		
		try {
			Long employeeId = leaveReq.getEmployee().getId();
			hasAccess = employeeService.isLoggedInUserHasAccessToThisEmployee(employeeId, false);
			if(!hasAccess) {
				return new ResponseEntity<Message>(new Message("You don't have permission to take action on this leave request."), HttpStatus.FORBIDDEN);
			}
			
			if(leaveReq.getStatus().equals(LeaveStatus.REQUESTED.name())) {
				if(action.equals(LeaveStatus.REJECTED.name())) {
					leaveReq.setStatus(LeaveStatus.REJECTED);
					leaveReq.setResponseDate(new Date());
					leaveReq.setActionTakenBy(loggedInUSerEmail);
					
					leaveRequestRecordRepo.save(leaveReq);
				}
				
				if(leaveReq.getStatus().equals(LeaveStatus.ACCEPTED.name())) {
					leaveReq.setStatus(LeaveStatus.ACCEPTED);
					leaveReq.setResponseDate(new Date());
					leaveReq.setActionTakenBy(loggedInUSerEmail);
					
					leaveRequestRecordRepo.saveAndFlush(leaveReq);
					
					LeaveCountDetails leaveCountDetails = null;
					Optional<LeaveCountDetails> optLeaveCntDtl = leaveCountDetailsRepo.findByEmployeeId(employeeId);
					if(optLeaveCntDtl.isPresent()) {
						leaveCountDetails = optLeaveCntDtl.get();
						Integer totalRequestedLeaves = leaveReq.getTotalLeaveDays();
						
						if(leaveReq.getLeaveType().equals(LeaveType.CASUAL_LEAVE.name())) {
							Double remainingCasualLeaves = leaveCountDetails.getRemainingCasualLeave();
							
							if(totalRequestedLeaves <= remainingCasualLeaves) {
								remainingCasualLeaves = remainingCasualLeaves - totalRequestedLeaves;
								leaveCountDetails.setRemainingCasualLeave(remainingCasualLeaves);
							}
							else {
								int totalLop = (int) (totalRequestedLeaves - remainingCasualLeaves);
								leaveCountDetails.setRemainingCasualLeave(0d);
								leaveCountDetails.setTotalLOP(totalLop);
							}
							leaveCountDetails.setRemainingLeaves(Double.parseDouble(totalRequestedLeaves.toString()));
						}
						
						else if(leaveReq.getLeaveType().equals(LeaveType.SICK_LEAVE.name())) {
							Double remainingSickLeaves = leaveCountDetails.getRemainingSickLeave();
							
							if(totalRequestedLeaves <= remainingSickLeaves) {
								remainingSickLeaves = remainingSickLeaves - totalRequestedLeaves;
								leaveCountDetails.setRemainingSickLeave(remainingSickLeaves);
							}
							else {
								int totalLop = (int) (totalRequestedLeaves - remainingSickLeaves);
								leaveCountDetails.setRemainingSickLeave(0d);
								leaveCountDetails.setTotalLOP(totalLop);
							}
							leaveCountDetails.setRemainingLeaves(Double.parseDouble(totalRequestedLeaves.toString()));
						}
						else {
							Double remainingEarnLeaves = leaveCountDetails.getRemainingEarnLeave();
							
							if(totalRequestedLeaves <= remainingEarnLeaves) {
								remainingEarnLeaves = remainingEarnLeaves - totalRequestedLeaves;
								leaveCountDetails.setRemainingEarnLeave(remainingEarnLeaves);
							}
							else {
								int totalLop = (int) (totalRequestedLeaves - remainingEarnLeaves);
								leaveCountDetails.setRemainingEarnLeave(0d);
								leaveCountDetails.setTotalLOP(totalLop);
							}
							leaveCountDetails.setRemainingLeaves(Double.parseDouble(totalRequestedLeaves.toString()));
						}
					}
					
					leaveCountDetailsRepo.save(leaveCountDetails);
				}
			}
			else {
				return new ResponseEntity<Message>(new Message("This leave request is already " + leaveReq.getStatus()), HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception ex) {
			logger.debug("ERROR : On leave count update from method leaveApproveOrRejectAction");
			logger.debug("ERROR : Error message is : " + ex.getMessage());
			throw new Exception("ERROR : On leave count update from method leaveApproveOrRejectAction", ex);
		}
		
		return new ResponseEntity<Message>(new Message("Requested leave " + action + " successfully."), HttpStatus.OK);
		
		
		
	}
}
