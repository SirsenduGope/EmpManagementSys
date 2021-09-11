package com.management.employee.payload;

import com.management.employee.entity.LeaveRecord;

public class LeaveRequest {

	private String employeeId;
	private LeaveRecord leaveRecord;
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public LeaveRecord getLeaveRecord() {
		return leaveRecord;
	}
	public void setLeaveRecord(LeaveRecord leaveRecord) {
		this.leaveRecord = leaveRecord;
	}
	
}
