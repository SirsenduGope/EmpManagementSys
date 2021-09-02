package com.management.employee.payload;

import java.util.HashSet;
import java.util.Set;

import com.management.employee.entity.Employee;
import com.management.employee.entity.EmployeeDetails;
import com.management.employee.entity.Role;

public class EmployeeResponse {

	private Employee emp;
	private EmployeeDetails empDetails;
	private Set<Role> roles = new HashSet<>();
	
	public EmployeeResponse() {
		super();
	}

	public EmployeeResponse(Employee emp, EmployeeDetails empDetails, Set<Role> roles) {
		super();
		this.emp = emp;
		this.empDetails = empDetails;
		this.roles = roles;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
	
	
}
