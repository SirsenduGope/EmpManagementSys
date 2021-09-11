package com.management.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.LeaveDetails;

@Repository
public interface LeaveDetailsRepository extends JpaRepository<LeaveDetails, Integer> {

	Optional<LeaveDetails> findByEmployeeId(Long Id);
	
}
