package com.management.employee.service;

import org.springframework.http.ResponseEntity;

import com.management.employee.entity.Employee;
import com.management.employee.entity.LeaveSettings;
import com.management.employee.payload.LeaveDetailsRequest;

public interface ILeaveService {

	public ResponseEntity<?> configureLeaveSettings(LeaveSettings leaveSettings) throws Exception;

	public ResponseEntity<?> generateLeaveDetailsForEmployee(Employee employee) throws Exception;

	public ResponseEntity<?> updateLeaveDetailsForEmployee(LeaveDetailsRequest leaveDetailsReq) throws Exception;


}
