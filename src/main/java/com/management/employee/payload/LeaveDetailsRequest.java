package com.management.employee.payload;

public class LeaveDetailsRequest {

	private String employeeId;
	private Double noOfCasualLeave;
	private Double remainingCasualLeave;
	private Double noOfSickLeave;
	private Double remainingSickLeave;
	private Double noOfEarnLeave;
	private Double remainingEarnLeave;
	private Double totalLeaves;
	private Double remainingLeaves;
	private Integer totalLOP;
	
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Double getNoOfCasualLeave() {
		return noOfCasualLeave;
	}
	public void setNoOfCasualLeave(Double noOfCasualLeave) {
		this.noOfCasualLeave = noOfCasualLeave;
	}
	public Double getRemainingCasualLeave() {
		return remainingCasualLeave;
	}
	public void setRemainingCasualLeave(Double remainingCasualLeave) {
		this.remainingCasualLeave = remainingCasualLeave;
	}
	public Double getNoOfSickLeave() {
		return noOfSickLeave;
	}
	public void setNoOfSickLeave(Double noOfSickLeave) {
		this.noOfSickLeave = noOfSickLeave;
	}
	public Double getRemainingSickLeave() {
		return remainingSickLeave;
	}
	public void setRemainingSickLeave(Double remainingSickLeave) {
		this.remainingSickLeave = remainingSickLeave;
	}
	public Double getNoOfEarnLeave() {
		return noOfEarnLeave;
	}
	public void setNoOfEarnLeave(Double noOfEarnLeave) {
		this.noOfEarnLeave = noOfEarnLeave;
	}
	public Double getRemainingEarnLeave() {
		return remainingEarnLeave;
	}
	public void setRemainingEarnLeave(Double remainingEarnLeave) {
		this.remainingEarnLeave = remainingEarnLeave;
	}
	public Double getTotalLeaves() {
		return totalLeaves;
	}
	public void setTotalLeaves(Double totalLeaves) {
		this.totalLeaves = totalLeaves;
	}
	public Double getRemainingLeaves() {
		return remainingLeaves;
	}
	public void setRemainingLeaves(Double remainingLeaves) {
		this.remainingLeaves = remainingLeaves;
	}
	public Integer getTotalLOP() {
		return totalLOP;
	}
	public void setTotalLOP(Integer totalLOP) {
		this.totalLOP = totalLOP;
	}
	
	
	
}
