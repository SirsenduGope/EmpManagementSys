package com.management.employee.payload;

import com.management.employee.entity.LeaveDetails;

public class LeaveDetailsRequest {

	private String employeeId;
	private LeaveDetails leaveDetails;
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public LeaveDetails getLeaveDetails() {
		return leaveDetails;
	}
	public void setLeaveDetails(LeaveDetails leaveDetails) {
		this.leaveDetails = leaveDetails;
	}
	
	
}
