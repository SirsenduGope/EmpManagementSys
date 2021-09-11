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
	private boolean casualLeaveMonthly = Boolean.FALSE;
	
	@Column(name = "sick_leave_MN")
	private boolean sickLeaveMonthly = Boolean.TRUE;
	
	@Column(name = "earn_leave_MN")
	private boolean earnLeaveMonthly = Boolean.TRUE;
	
	@Column(name = "casual_leave_CF")
	private boolean casualLeaveCarryFwd = Boolean.FALSE;
	
	@Column(name = "sick_leave_CF")
	private boolean sickLeaveCarryFwd = Boolean.FALSE;
	
	@Column(name = "earn_leave_CF")
	private boolean earnLeaveCarryFwd = Boolean.TRUE;
	
	@Column(name = "sat_working_day")
	private boolean saturdayWorkingDay = Boolean.FALSE;
	
	@Column(name = "sun_working_day")
	private boolean sundayWorkingDay = Boolean.FALSE;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public LeaveSettings() {
		super();
	}

	public LeaveSettings(Double casualLeaveCount, Double sickLeaveCount, Double earnLeaveCount,
			boolean casualLeaveMonthly, boolean sickLeaveMonthly, boolean earnLeaveMonthly, boolean casualLeaveCarryFwd,
			boolean sickLeaveCarryFwd, boolean earnLeaveCarryFwd, boolean saturdayWorkingDay,
			boolean sundayWorkingDay) {
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

	public boolean isCasualLeaveMonthly() {
		return casualLeaveMonthly;
	}

	public void setCasualLeaveMonthly(boolean casualLeaveMonthly) {
		this.casualLeaveMonthly = casualLeaveMonthly;
	}

	public boolean isSickLeaveMonthly() {
		return sickLeaveMonthly;
	}

	public void setSickLeaveMonthly(boolean sickLeaveMonthly) {
		this.sickLeaveMonthly = sickLeaveMonthly;
	}

	public boolean isEarnLeaveMonthly() {
		return earnLeaveMonthly;
	}

	public void setEarnLeaveMonthly(boolean earnLeaveMonthly) {
		this.earnLeaveMonthly = earnLeaveMonthly;
	}

	public boolean isCasualLeaveCarryFwd() {
		return casualLeaveCarryFwd;
	}

	public void setCasualLeaveCarryFwd(boolean casualLeaveCarryFwd) {
		this.casualLeaveCarryFwd = casualLeaveCarryFwd;
	}

	public boolean isSickLeaveCarryFwd() {
		return sickLeaveCarryFwd;
	}

	public void setSickLeaveCarryFwd(boolean sickLeaveCarryFwd) {
		this.sickLeaveCarryFwd = sickLeaveCarryFwd;
	}

	public boolean isEarnLeaveCarryFwd() {
		return earnLeaveCarryFwd;
	}

	public void setEarnLeaveCarryFwd(boolean earnLeaveCarryFwd) {
		this.earnLeaveCarryFwd = earnLeaveCarryFwd;
	}

	public boolean isSaturdayWorkingDay() {
		return saturdayWorkingDay;
	}

	public void setSaturdayWorkingDay(boolean saturdayWorkingDay) {
		this.saturdayWorkingDay = saturdayWorkingDay;
	}

	public boolean isSundayWorkingDay() {
		return sundayWorkingDay;
	}

	public void setSundayWorkingDay(boolean sundayWorkingDay) {
		this.sundayWorkingDay = sundayWorkingDay;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "LeaveSettings [id=" + id + ", casualLeaveCount=" + casualLeaveCount + ", sickLeaveCount="
				+ sickLeaveCount + ", earnLeaveCount=" + earnLeaveCount + ", casualLeaveMonthly=" + casualLeaveMonthly
				+ ", sickLeaveMonthly=" + sickLeaveMonthly + ", earnLeaveMonthly=" + earnLeaveMonthly
				+ ", casualLeaveCarryFwd=" + casualLeaveCarryFwd + ", sickLeaveCarryFwd=" + sickLeaveCarryFwd
				+ ", earnLeaveCarryFwd=" + earnLeaveCarryFwd + ", saturdayWorkingDay=" + saturdayWorkingDay
				+ ", sundayWorkingDay=" + sundayWorkingDay + ", deleted=" + deleted + "]";
	}

	
	
	
	
}
