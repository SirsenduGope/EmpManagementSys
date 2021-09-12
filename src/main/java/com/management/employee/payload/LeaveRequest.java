package com.management.employee.payload;

import com.management.employee.entity.LeaveRequestRecord;

public class LeaveRequest {

	private String employeeId;
	private LeaveRequestRecord leaveRecord;
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public LeaveRequestRecord getLeaveRecord() {
		return leaveRecord;
	}
	public void setLeaveRecord(LeaveRequestRecord leaveRecord) {
		this.leaveRecord = leaveRecord;
	}
	
}
