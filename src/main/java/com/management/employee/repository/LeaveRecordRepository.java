package com.management.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.LeaveRecord;

@Repository
public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Integer> {

	
}
