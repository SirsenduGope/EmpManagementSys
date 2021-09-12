package com.management.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.LeaveRequestRecord;

@Repository
public interface LeaveRequestRecordRepository extends JpaRepository<LeaveRequestRecord, Integer> {

	Optional<LeaveRequestRecord> findByIdAndEmployeeId(Integer id, Long empId);
	
	Optional<List<LeaveRequestRecord>> findAllByEmployeeIdIn(List<Long> ids);
	
	Optional<List<LeaveRequestRecord>> findByLeaveType(String leaveType);
	
	Optional<List<LeaveRequestRecord>> findByLeaveTypeAndEmployeeIdIn(String leaveType, List<Long> employeeIds);
	
	Optional<List<LeaveRequestRecord>> findByLeaveStatus(String leaveStatus);
	
	Optional<List<LeaveRequestRecord>> findByLeaveStatusAndEmployeeIdIn(String leaveStatus, List<Long> employeeIds);
	
	Optional<List<LeaveRequestRecord>> findByLeaveTypeAndLeaveStatus(String leaveType, String leaveStatus);
	
	Optional<List<LeaveRequestRecord>> findByLeaveTypeAndLeaveStatusAndEmployeeIdIn(String leaveType, String leaveStatus, List<Long> employeeIds);
	
}
