package com.management.employee.service;

import org.springframework.http.ResponseEntity;

import com.management.employee.entity.Employee;
import com.management.employee.entity.LeaveSettings;
import com.management.employee.payload.LeaveDetailsRequest;
import com.management.employee.payload.LeaveRequest;

public interface ILeaveService {

	public ResponseEntity<?> configureLeaveSettings(LeaveSettings leaveSettings) throws Exception;

	public ResponseEntity<?> generateLeaveCountDetailsForEmployee(Employee employee) throws Exception;

	public ResponseEntity<?> updateLeaveCountDetailsForEmployee(LeaveDetailsRequest leaveDetailsReq) throws Exception;

	public ResponseEntity<?> requestForLeave(LeaveRequest leaveReq) throws Exception;

	public ResponseEntity<?> updateLeaveRequestRecord(LeaveRequest leaveRequest) throws Exception;

	public ResponseEntity<?> getAllRequetedLeaves(String leaveType, String leaveStatus) throws Exception;

	public ResponseEntity<?> getAllMyLeaves(String leaveType, String leaveStatus) throws Exception;

	public ResponseEntity<?> getLeaveRequestRecordById(String id) throws Exception;


}
