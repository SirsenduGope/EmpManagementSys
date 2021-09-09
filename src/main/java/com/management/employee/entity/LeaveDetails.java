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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "leave_details")
@SQLDelete(sql = "UPDATE leave_details SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class LeaveDetails extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = 953578744714667214L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id")
	@JsonIgnoreProperties(value = {"leave_details", "hibernateLazyInitializer"})
	private Employee employee;
	
	@Column(name = "total_casual_leave")
	private Integer noOfCasualLeave;
	
	@Column(name = "total_sick_leave")
	private Integer noOfSickLeave;
	
	@Column(name = "total_earn_leave")
	private Integer noOfEarnLeave;
	
	@Column(name = "total_leaves")
	private Integer totalLeaves;
	
	@Column(name = "remaining_leaves")
	private Integer remainingLeaves;
	
	@Column(name = "prv_year_carry")
	private Integer carryFromPrevYear;
	
	@Column(name = "total_lop")
	private Integer totalLOP;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public LeaveDetails() {
		super();
	}

	public LeaveDetails(Employee employee, Integer noOfCasualLeave, Integer noOfSickLeave, Integer noOfEarnLeave,
			Integer totalLeaves, Integer remainingLeaves, Integer carryFromPrevYear, Integer totalLOP) {
		super();
		this.employee = employee;
		this.noOfCasualLeave = noOfCasualLeave;
		this.noOfSickLeave = noOfSickLeave;
		this.noOfEarnLeave = noOfEarnLeave;
		this.totalLeaves = totalLeaves;
		this.remainingLeaves = remainingLeaves;
		this.carryFromPrevYear = carryFromPrevYear;
		this.totalLOP = totalLOP;
	}

	public Integer getId() {
		return id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Integer getNoOfCasualLeave() {
		return noOfCasualLeave;
	}

	public void setNoOfCasualLeave(Integer noOfCasualLeave) {
		this.noOfCasualLeave = noOfCasualLeave;
	}

	public Integer getNoOfSickLeave() {
		return noOfSickLeave;
	}

	public void setNoOfSickLeave(Integer noOfSickLeave) {
		this.noOfSickLeave = noOfSickLeave;
	}

	public Integer getNoOfEarnLeave() {
		return noOfEarnLeave;
	}

	public void setNoOfEarnLeave(Integer noOfEarnLeave) {
		this.noOfEarnLeave = noOfEarnLeave;
	}

	public Integer getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Integer totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	public Integer getRemainingLeaves() {
		return remainingLeaves;
	}

	public void setRemainingLeaves(Integer remainingLeaves) {
		this.remainingLeaves = remainingLeaves;
	}

	public Integer getCarryFromPrevYear() {
		return carryFromPrevYear;
	}

	public void setCarryFromPrevYear(Integer carryFromPrevYear) {
		this.carryFromPrevYear = carryFromPrevYear;
	}

	public Integer getTotalLOP() {
		return totalLOP;
	}

	public void setTotalLOP(Integer totalLOP) {
		this.totalLOP = totalLOP;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
}
