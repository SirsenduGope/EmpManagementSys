package com.management.employee.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "employee_status")
@SQLDelete(sql = "UPDATE employee_status SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class EmployeeStatus extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = -7612435387734737773L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "status", unique = true, nullable = false, length = 100)
	private String status;
	
	@OneToOne(mappedBy = "empStatus")
	private EmployeeDetails empDetails;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public EmployeeStatus() {
		super();
	}
	
	public EmployeeStatus(String status) {
		super();
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		
}
