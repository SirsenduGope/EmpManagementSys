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
	
	@Column(name = "casual_leave_count")
	private Double casualLeaveCount = 0d;
	
	@Column(name = "sick_leave_count")
	private Double sickLeaveCount = 0d;
	
	@Column(name = "earn_leave_count")
	private Double earnLeaveCount = 0d;
	
	@Column(name = "casual_leave_MN")
	private Boolean casualLeaveMonthly = Boolean.FALSE;
	
	@Column(name = "sick_leave_MN")
	private Boolean sickLeaveMonthly = Boolean.TRUE;
	
	@Column(name = "earn_leave_MN")
	private Boolean earnLeaveMonthly = Boolean.TRUE;
	
	@Column(name = "casual_leave_CF")
	private Boolean casualLeaveCarryFwd = Boolean.FALSE;
	
	@Column(name = "sick_leave_CF")
	private Boolean sickLeaveCarryFwd = Boolean.FALSE;
	
	@Column(name = "earn_leave_CF")
	private Boolean earnLeaveCarryFwd = Boolean.TRUE;
	
	@Column(name = "sat_working_day")
	private Boolean saturdayWorkingDay = Boolean.FALSE;
	
	@Column(name = "sun_working_day")
	private Boolean sundayWorkingDay = Boolean.FALSE;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public LeaveSettings() {
		super();
	}

	public LeaveSettings(Double casualLeaveCount, Double sickLeaveCount, Double earnLeaveCount,
			Boolean casualLeaveMonthly, Boolean sickLeaveMonthly, Boolean earnLeaveMonthly, Boolean casualLeaveCarryFwd,
			Boolean sickLeaveCarryFwd, Boolean earnLeaveCarryFwd, Boolean saturdayWorkingDay, Boolean sundayWorkingDay,
			boolean deleted) {
		super();
		this.casualLeaveCount = casualLeaveCount;
		this.sickLeaveCount = sickLeaveCount;
		this.earnLeaveCount = earnLeaveCount;
		this.casualLeaveMonthly = casualLeaveMonthly;
		this.sickLeaveMonthly = sickLeaveMonthly;
		this.earnLeaveMonthly = earnLeaveMonthly;
		this.casualLeaveCarryFwd = casualLeaveCarryFwd;
		this.sickLeaveCarryFwd = sickLeaveCarryFwd;
		this.earnLeaveCarryFwd = earnLeaveCarryFwd;
		this.saturdayWorkingDay = saturdayWorkingDay;
		this.sundayWorkingDay = sundayWorkingDay;
		this.deleted = deleted;
	}

	public Integer getId() {
		return id;
	}

	public Double getCasualLeaveCount() {
		return casualLeaveCount;
	}

	public void setCasualLeaveCount(Double casualLeaveCount) {
		this.casualLeaveCount = casualLeaveCount;
	}

	public Double getSickLeaveCount() {
		return sickLeaveCount;
	}

	public void setSickLeaveCount(Double sickLeaveCount) {
		this.sickLeaveCount = sickLeaveCount;
	}

	public Double getEarnLeaveCount() {
		return earnLeaveCount;
	}

	public void setEarnLeaveCount(Double earnLeaveCount) {
		this.earnLeaveCount = earnLeaveCount;
	}

	public Boolean getCasualLeaveMonthly() {
		return casualLeaveMonthly;
	}

	public void setCasualLeaveMonthly(Boolean casualLeaveMonthly) {
		this.casualLeaveMonthly = casualLeaveMonthly;
	}

	public Boolean getSickLeaveMonthly() {
		return sickLeaveMonthly;
	}

	public void setSickLeaveMonthly(Boolean sickLeaveMonthly) {
		this.sickLeaveMonthly = sickLeaveMonthly;
	}

	public Boolean getEarnLeaveMonthly() {
		return earnLeaveMonthly;
	}

	public void setEarnLeaveMonthly(Boolean earnLeaveMonthly) {
		this.earnLeaveMonthly = earnLeaveMonthly;
	}

	public Boolean getCasualLeaveCarryFwd() {
		return casualLeaveCarryFwd;
	}

	public void setCasualLeaveCarryFwd(Boolean casualLeaveCarryFwd) {
		this.casualLeaveCarryFwd = casualLeaveCarryFwd;
	}

	public Boolean getSickLeaveCarryFwd() {
		return sickLeaveCarryFwd;
	}

	public void setSickLeaveCarryFwd(Boolean sickLeaveCarryFwd) {
		this.sickLeaveCarryFwd = sickLeaveCarryFwd;
	}

	public Boolean getEarnLeaveCarryFwd() {
		return earnLeaveCarryFwd;
	}

	public void setEarnLeaveCarryFwd(Boolean earnLeaveCarryFwd) {
		this.earnLeaveCarryFwd = earnLeaveCarryFwd;
	}

	public Boolean getSaturdayWorkingDay() {
		return saturdayWorkingDay;
	}

	public void setSaturdayWorkingDay(Boolean saturdayWorkingDay) {
		this.saturdayWorkingDay = saturdayWorkingDay;
	}

	public Boolean getSundayWorkingDay() {
		return sundayWorkingDay;
	}

	public void setSundayWorkingDay(Boolean sundayWorkingDay) {
		this.sundayWorkingDay = sundayWorkingDay;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
}
