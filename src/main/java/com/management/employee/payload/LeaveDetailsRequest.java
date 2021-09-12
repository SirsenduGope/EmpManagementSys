package com.management.employee.payload;

import com.management.employee.entity.LeaveCountDetails;

public class LeaveDetailsRequest {

	private String employeeId;
	private LeaveCountDetails leaveDetails;
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public LeaveCountDetails getLeaveDetails() {
		return leaveDetails;
	}
	public void setLeaveDetails(LeaveCountDetails leaveDetails) {
		this.leaveDetails = leaveDetails;
	}
	
	
}
