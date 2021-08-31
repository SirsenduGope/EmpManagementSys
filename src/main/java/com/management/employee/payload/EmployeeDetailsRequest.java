package com.management.employee.payload;

import com.management.employee.entity.EmployeeDetails;

public class EmployeeDetailsRequest {
	
	private String id;
	private EmployeeDetails employeeDetails;
	
	
	public EmployeeDetailsRequest() {
		super();
	}
	
	
	public EmployeeDetailsRequest(String id, EmployeeDetails employeeDetails) {
		super();
		this.id = id;
		this.employeeDetails = employeeDetails;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public EmployeeDetails getEmployeeDetails() {
		return employeeDetails;
	}
	public void setEmployeeDetails(EmployeeDetails employeeDetails) {
		this.employeeDetails = employeeDetails;
	}
	
	

}
