package com.management.employee.payload;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.employee.entity.Employee;
import com.management.employee.entity.EmployeeDetails;
import com.management.employee.entity.Role;
import com.management.employee.entity.Views;

public class EmployeeResponse {

	@JsonView(Views.Public.class)
	private Employee emp;
	
	@JsonView(Views.Public.class)
	private EmployeeDetails empDetails;
	
	@JsonView(Views.Public.class)
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
