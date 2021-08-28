package com.management.employee.payload;

import com.management.employee.entity.Employee;
import com.management.employee.entity.EmployeeDetails;

public class EmployeeResponse {

	private Employee emp;
	private EmployeeDetails empDetails;
	
	public EmployeeResponse() {
		super();
	}

	public EmployeeResponse(Employee emp, EmployeeDetails empDetails) {
		super();
		this.emp = emp;
		this.empDetails = empDetails;
	}

	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
	}
	
	
	
	
}
