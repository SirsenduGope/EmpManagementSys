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
	private Double noOfCasualLeave = 0d;
	
	@Column(name = "remaining_casual_leave")
	private Double remainingCasualLeave = 0d;
	
	@Column(name = "total_sick_leave")
	private Double noOfSickLeave = 0d;
	
	@Column(name = "remaining_sick_leave")
	private Double remainingSickLeave = 0d;
	
	@Column(name = "total_earn_leave")
	private Double noOfEarnLeave = 0d;
	
	@Column(name = "remaining_earn_leave")
	private Double remainingEarnLeave = 0d;
	
	@Column(name = "total_leaves")
	private Double totalLeaves = 0d;
	
	@Column(name = "remaining_leaves")
	private Double remainingLeaves = 0d;
	
	@Column(name = "total_lop")
	private Integer totalLOP = 0;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public LeaveDetails() {
		super();
	}

	public LeaveDetails(Employee employee, Double noOfCasualLeave, Double remainingCasualLeave, Double noOfSickLeave,
			Double remainingSickLeave, Double noOfEarnLeave, Double remainingEarnLeave, Double totalLeaves,
			Double remainingLeaves, Integer totalLOP) {
		super();
		this.employee = employee;
		this.noOfCasualLeave = noOfCasualLeave;
		this.remainingCasualLeave = remainingCasualLeave;
		this.noOfSickLeave = noOfSickLeave;
		this.remainingSickLeave = remainingSickLeave;
		this.noOfEarnLeave = noOfEarnLeave;
		this.remainingEarnLeave = remainingEarnLeave;
		this.totalLeaves = totalLeaves;
		this.remainingLeaves = remainingLeaves;
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

	public Double getNoOfCasualLeave() {
		return noOfCasualLeave;
	}

	public void setNoOfCasualLeave(Double noOfCasualLeave) {
		this.noOfCasualLeave = noOfCasualLeave;
	}

	public Double getRemainingCasualLeave() {
		return remainingCasualLeave;
	}

	public void setRemainingCasualLeave(Double remainingCasualLeave) {
		this.remainingCasualLeave = remainingCasualLeave;
	}

	public Double getNoOfSickLeave() {
		return noOfSickLeave;
	}

	public void setNoOfSickLeave(Double noOfSickLeave) {
		this.noOfSickLeave = noOfSickLeave;
	}

	public Double getRemainingSickLeave() {
		return remainingSickLeave;
	}

	public void setRemainingSickLeave(Double remainingSickLeave) {
		this.remainingSickLeave = remainingSickLeave;
	}

	public Double getNoOfEarnLeave() {
		return noOfEarnLeave;
	}

	public void setNoOfEarnLeave(Double noOfEarnLeave) {
		this.noOfEarnLeave = noOfEarnLeave;
	}

	public Double getRemainingEarnLeave() {
		return remainingEarnLeave;
	}

	public void setRemainingEarnLeave(Double remainingEarnLeave) {
		this.remainingEarnLeave = remainingEarnLeave;
	}

	public Double getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Double totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	public Double getRemainingLeaves() {
		return remainingLeaves;
	}

	public void setRemainingLeaves(Double remainingLeaves) {
		this.remainingLeaves = remainingLeaves;
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

	@Override
	public String toString() {
		return "LeaveDetails [id=" + id + ", employee=" + employee + ", noOfCasualLeave=" + noOfCasualLeave
				+ ", remainingCasualLeave=" + remainingCasualLeave + ", noOfSickLeave=" + noOfSickLeave
				+ ", remainingSickLeave=" + remainingSickLeave + ", noOfEarnLeave=" + noOfEarnLeave
				+ ", remainingEarnLeave=" + remainingEarnLeave + ", totalLeaves=" + totalLeaves + ", remainingLeaves="
				+ remainingLeaves + ", totalLOP=" + totalLOP + ", deleted=" + deleted + "]";
	}
	
	

	
}
