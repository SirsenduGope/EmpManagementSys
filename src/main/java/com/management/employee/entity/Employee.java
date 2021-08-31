package com.management.employee.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "employees")
@SQLDelete(sql = "UPDATE employees SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Employee extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = -7612435387734737773L;
	
	@Id
	@JsonView(Views.Public.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@JsonView(Views.Public.class)
	@Column(name = "email", unique = true, nullable = false, length = 100)
	private String email;
	
	@JsonView(Views.Internal.class)
	@Column(name = "password", nullable = false, length = 150)
	private String password;
	
	@JsonView(Views.Public.class)
	@ManyToOne
    @JoinColumn(name = "report_to", referencedColumnName = "email")
	private Employee manager;
	
	@JsonIgnore
	@JsonView(Views.Public.class)
	@OneToMany(mappedBy = "manager")
	private List<Employee> subordinates = new ArrayList<Employee>();
	
	@JsonView(Views.Public.class)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "emp_details_id")
	@JsonIgnoreProperties(value = {"employees", "hibernateLazyInitializer"})
	private EmployeeDetails employeeDetails;
		
	@JsonView(Views.Public.class)
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(	name = "employee_roles", 
				joinColumns = @JoinColumn(name = "employee_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public Employee() {
		super();
	}

	public Employee(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public Employee(String email, String password, EmployeeDetails employeeDetails, Set<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.employeeDetails = employeeDetails;
		this.roles = roles;
	}

	public Employee(String email, String password, Employee manager, EmployeeDetails employeeDetails, Set<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.manager = manager;
		this.employeeDetails = employeeDetails;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public EmployeeDetails getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(EmployeeDetails employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}


	/*
	 * @Override public String toString() { return "Employee [id=" + id + ", email="
	 * + email + ", password=" + password + ", manager=" + manager.getEmail() +
	 * ", subordinates=" + subordinates + ", employeeDetails=" + employeeDetails +
	 * ", roles=" + roles + ", deleted=" + deleted + "]"; }
	 */

	
	
	
	
}
