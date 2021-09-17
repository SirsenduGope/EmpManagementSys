package com.management.employee.entity;

import java.io.Serializable;
import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.management.employee.enums.LeaveStatus;
import com.management.employee.enums.LeaveType;

@Entity
@Table(name = "leave_request_record")
@SQLDelete(sql = "UPDATE leave_request_record SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class LeaveRequestRecord extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = -1041378941673679898L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id")
	@JsonIgnoreProperties(value = {"leave_record", "hibernateLazyInitializer"})
	private Employee employee;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@Column(name = "from_date", nullable = false)
	private Date fromDate;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@Column(name = "to_date", nullable = false)
	private Date toDate;
	
	@Column(name = "total_days", nullable = false)
	private Integer totalLeaveDays;
	
	@Column(name = "leave_reason", length = 500)
	private String leaveReason;
	
	@Column(name = "leave_status", nullable = false)
	private LeaveStatus leaveStatus;
	
	@Column(name = "leave_type", nullable = false)
	private LeaveType leaveType;
	
	@Column(name = "request_date")
	private Date requestDate;
	
	@Column(name = "response_date")
	private Date responseDate;
	
	@Column(name = "action_taken_by")
	private String actionTakenBy;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public LeaveRequestRecord() {
		super();
	}

	public LeaveRequestRecord(Date fromDate, Date toDate, String leaveReason, LeaveStatus leaveStatus) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.leaveReason = leaveReason;
		this.leaveStatus = leaveStatus;
	}
	
	public LeaveRequestRecord(Employee employee, Date fromDate, Date toDate, String leaveReason,
			Integer totalLeaveDays, LeaveStatus leaveStatus, LeaveType leaveType, Date requestDate, 
			Date responseDate, String actionTakenBy) {
		super();
		this.employee = employee;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.leaveReason = leaveReason;
		this.totalLeaveDays = totalLeaveDays;
		this.leaveStatus = leaveStatus;
		this.leaveType = leaveType;
		this.requestDate = requestDate;
		this.responseDate = responseDate;
		this.actionTakenBy = actionTakenBy;
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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public Integer getTotalLeaveDays() {
		return totalLeaveDays;
	}

	public void setTotalLeaveDays(Integer totalLeaveDays) {
		this.totalLeaveDays = totalLeaveDays;
	}
	
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public String getActionTakenBy() {
		return actionTakenBy;
	}

	public void setActionTakenBy(String actionTakenBy) {
		this.actionTakenBy = actionTakenBy;
	}

	@Override
	public String toString() {
		return "LeaveRecord [id=" + id + ", employee=" + employee + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", totalLeaveDays=" + totalLeaveDays + ", leaveReason=" + leaveReason + ", leaveStatus=" + leaveStatus
				+ ", leaveType=" + leaveType + ", requestDate=" + requestDate + ", responseDate=" + responseDate
				+ ", deleted=" + deleted + "]";
	}


}
