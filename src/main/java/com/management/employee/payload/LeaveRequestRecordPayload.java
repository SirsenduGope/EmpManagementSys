package com.management.employee.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.management.employee.enums.LeaveType;

public class LeaveRequestRecordPayload {

	private Integer id;
	
	private Long employeeId;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date fromDate;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date toDate;
	
	private Integer totalLeaveDays;
	
	private String leaveReason;
	
	private LeaveType leaveType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getTotalLeaveDays() {
		return totalLeaveDays;
	}

	public void setTotalLeaveDays(Integer totalLeaveDays) {
		this.totalLeaveDays = totalLeaveDays;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	
	
	
}
