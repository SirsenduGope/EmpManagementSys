package com.management.employee.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "organization_details")
@SQLDelete(sql = "UPDATE organization_details SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class OrganizationDetails extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = 4175435672520939561L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "name", nullable = false, length = 100)
	private String orgName;
	
	@Column(name = "owner", nullable = false, length = 100)
	private String ownerEmail;
	
	@OneToMany(mappedBy = "orgDetails")
	private List<Employee> employee;
	
	@OneToMany(mappedBy = "orgDetails")
	private List<EmployeeDetails> empDetails;
	
	@OneToMany(mappedBy = "orgDetails")
	private List<LeaveDetails> leaveDetails;
	
	@OneToMany(mappedBy = "orgDetails")
	private List<LeaveRecord> leaveRecords;
	
	@OneToMany(mappedBy = "orgDetails")
	private List<LeaveSettings> leaveSettings;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public OrganizationDetails() {
		super();
	}

	public OrganizationDetails(String orgName, String ownerEmail) {
		super();
		this.orgName = orgName;
		this.ownerEmail = ownerEmail;
	}

	public Integer getId() {
		return id;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
