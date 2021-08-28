package com.management.employee.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.employee.enums.Gender;

@Entity
@Table(name = "employee_details")
@SQLDelete(sql = "UPDATE employee_details SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class EmployeeDetails extends Auditable<String> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7975042137908448876L;

	@Id
	@JsonView(Views.Public.class)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@JsonView(Views.Public.class)
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;
	
	@JsonView(Views.Public.class)
	@Column(name = "middle_name", length = 50)
	private String middleName;
	
	@JsonView(Views.Public.class)
	@Column(name = "last_name", length = 50)
	private String lastName;
	
	@JsonView(Views.Public.class)
	@Column(name = "phone", length = 15)
	private String phone;
	
	@JsonView(Views.Public.class)
	@Column(name = "mobile", length = 15)
	private String mobile;
	
	@JsonView(Views.Public.class)
	@Column(name = "address", length = 150)
	private String address;
	
	@JsonView(Views.Public.class)
	@Column(name = "state", length = 100)
	private String state;
	
	@JsonView(Views.Public.class)
	@Column(name = "city", length = 100)
	private String city;
	
	@JsonView(Views.Public.class)
	@Column(name = "zip", length = 10)
	private String zip;
	
	@JsonView(Views.Public.class)
	@Column(name = "gender", length = 10)
	private Gender gender;
	
	@JsonView(Views.Public.class)
	@Column(name = "date_of_joining")
	private Date dateOfJoining;
	
	@JsonView(Views.Public.class)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@JsonView(Views.Public.class)
	@Column(name = "is_active", columnDefinition = "bit(1) default b'1'")
	private boolean isActive = Boolean.TRUE;

	@JsonView(Views.Internal.class)
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;
	
	@OneToOne(mappedBy = "employeeDetails")
	private Employee employee;

	public EmployeeDetails() {
		super();
	}

	public EmployeeDetails(Long id, String firstName, String middleName, String lastName, String phone, String mobile,
			String address, String state, String city, String zip, Gender gender, Date dateOfJoining, Date dateOfBirth,
			boolean isActive, boolean deleted, Employee employee) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.phone = phone;
		this.mobile = mobile;
		this.address = address;
		this.state = state;
		this.city = city;
		this.zip = zip;
		this.gender = gender;
		this.dateOfJoining = dateOfJoining;
		this.dateOfBirth = dateOfBirth;
		this.isActive = isActive;
		this.deleted = deleted;
		this.employee = employee;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "EmployeeDetails [id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", phone=" + phone + ", mobile=" + mobile + ", address=" + address + ", state=" + state
				+ ", city=" + city + ", zip=" + zip + ", gender=" + gender + ", dateOfJoining=" + dateOfJoining
				+ ", dateOfBirth=" + dateOfBirth + ", isActive=" + isActive + ", deleted=" + deleted + "]";
	}

	
	

		
}
