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
@Table(name = "designation")
@SQLDelete(sql = "UPDATE designation SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Designation extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = -7612435387734737773L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "designation", unique = true, nullable = false, length = 100)
	private String designation;
	
	
	@OneToMany(mappedBy = "designation")
	private List<EmployeeDetails> empDetails;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public Designation() {
		super();
	}
	
	public Designation(String designation) {
		super();
		this.designation = designation;
	}

	public Integer getId() {
		return id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

		
}
