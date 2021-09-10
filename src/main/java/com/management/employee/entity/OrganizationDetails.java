package com.management.employee.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "organization_details")
@SQLDelete(sql = "UPDATE organization_details SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class OrganizationDetails extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = -2721659458168378577L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public OrganizationDetails() {
		super();
	}

	public OrganizationDetails(String name) {
		super();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
}
