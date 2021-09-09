package com.management.employee.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "leave_settings")
@SQLDelete(sql = "UPDATE leave_settings SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class LeaveSettings extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = 2724043532338834155L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "org_id")
	@JsonIgnoreProperties(value = {"leave_settings", "hibernateLazyInitializer"})
	private OrganizationDetails orgDetails;
	
	@Column(name = "casual_leave_count")
	private String casualLeaveCount;
	
	@Column(name = "sick_leave_count")
	private String sickLeaveCount;
	
	@Column(name = "earn_leave_count")
	private String earnLeaveCount;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public LeaveSettings() {
		super();
	}

	public LeaveSettings(OrganizationDetails orgDetails, String casualLeaveCount, String sickLeaveCount,
			String earnLeaveCount) {
		super();
		this.orgDetails = orgDetails;
		this.casualLeaveCount = casualLeaveCount;
		this.sickLeaveCount = sickLeaveCount;
		this.earnLeaveCount = earnLeaveCount;
	}

	public Integer getId() {
		return id;
	}

	public OrganizationDetails getOrgDetails() {
		return orgDetails;
	}

	public void setOrgDetails(OrganizationDetails orgDetails) {
		this.orgDetails = orgDetails;
	}

	public String getCasualLeaveCount() {
		return casualLeaveCount;
	}

	public void setCasualLeaveCount(String casualLeaveCount) {
		this.casualLeaveCount = casualLeaveCount;
	}

	public String getSickLeaveCount() {
		return sickLeaveCount;
	}

	public void setSickLeaveCount(String sickLeaveCount) {
		this.sickLeaveCount = sickLeaveCount;
	}

	public String getEarnLeaveCount() {
		return earnLeaveCount;
	}

	public void setEarnLeaveCount(String earnLeaveCount) {
		this.earnLeaveCount = earnLeaveCount;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
}
