package com.management.employee.service;

import org.springframework.http.ResponseEntity;

import com.management.employee.entity.Employee;
import com.management.employee.entity.LeaveRequestRecord;
import com.management.employee.entity.LeaveSettings;
import com.management.employee.payload.LeaveDetailsRequest;

public interface ILeaveService {

	public ResponseEntity<?> configureLeaveSettings(LeaveSettings leaveSettings) throws Exception;

	public ResponseEntity<?> generateLeaveCountDetailsForEmployee(Employee employee) throws Exception;

	public ResponseEntity<?> updateLeaveCountDetailsForEmployee(LeaveDetailsRequest leaveDetailsReq) throws Exception;

	public ResponseEntity<?> requestForLeave(LeaveRequestRecord leaveReq) throws Exception;

	public ResponseEntity<?> updateLeaveRequestRecord(LeaveRequestRecord leaveRequest) throws Exception;

	public ResponseEntity<?> getAllRequetedLeaves(String leaveType, String leaveStatus) throws Exception;

	public ResponseEntity<?> getAllMyLeaves(String leaveType, String leaveStatus) throws Exception;

	public ResponseEntity<?> getLeaveRequestRecordById(String id) throws Exception;

	public ResponseEntity<?> leaveApproveOrRejectAction(String action, LeaveRequestRecord leaveReq) throws Exception;


}
