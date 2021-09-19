package com.management.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.LeaveRequestRecord;
import com.management.employee.enums.LeaveStatus;
import com.management.employee.enums.LeaveType;

@Repository
public interface LeaveRequestRecordRepository extends JpaRepository<LeaveRequestRecord, Integer> {

	Optional<LeaveRequestRecord> findByIdAndEmployeeId(Integer id, Long empId);
	
	Optional<List<LeaveRequestRecord>> findAllByEmployeeIdIn(List<Long> ids);
	
	Optional<List<LeaveRequestRecord>> findByLeaveType(LeaveType leaveType);
	
	Optional<List<LeaveRequestRecord>> findByLeaveTypeAndEmployeeIdIn(LeaveType leaveType, List<Long> employeeIds);
	
	Optional<List<LeaveRequestRecord>> findByLeaveStatus(LeaveStatus leaveStatus);
	
	Optional<List<LeaveRequestRecord>> findByLeaveStatusAndEmployeeIdIn(LeaveStatus leaveStatus, List<Long> employeeIds);
	
	Optional<List<LeaveRequestRecord>> findByLeaveTypeAndLeaveStatus(LeaveType leaveType, LeaveStatus leaveStatus);
	
	Optional<List<LeaveRequestRecord>> findByLeaveTypeAndLeaveStatusAndEmployeeIdIn(LeaveType leaveType, LeaveStatus leaveStatus, List<Long> employeeIds);
	
}
