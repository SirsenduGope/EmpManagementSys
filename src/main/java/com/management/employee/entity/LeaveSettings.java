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
@Table(name = "leave_settings")
@SQLDelete(sql = "UPDATE leave_settings SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class LeaveSettings extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = 2724043532338834155L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "leave_type")
	private String leaveType;
	
	@Column(name = "leave_per_month")
	private Integer leavePerMonth;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public LeaveSettings() {
		super();
	}

	public LeaveSettings(String leaveType, Integer leavePerMonth) {
		super();
		this.leaveType = leaveType;
		this.leavePerMonth = leavePerMonth;
	}

	public Integer getId() {
		return id;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public Integer getLeavePerMonth() {
		return leavePerMonth;
	}

	public void setLeavePerMonth(Integer leavePerMonth) {
		this.leavePerMonth = leavePerMonth;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
}
