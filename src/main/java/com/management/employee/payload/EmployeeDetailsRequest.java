package com.management.employee.payload;

import com.management.employee.entity.EmployeeDetails;

public class EmployeeDetailsRequest {
	
	private String id;
	private EmployeeDetails employeeDetails;
	private String designation;
	private String status;
	
	
	public EmployeeDetailsRequest() {
		super();
	}
	
	
	public EmployeeDetailsRequest(String id, EmployeeDetails employeeDetails,
			String designation, String status) {
		super();
		this.id = id;
		this.employeeDetails = employeeDetails;
		this.designation = designation;
		this.status = status;
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
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
