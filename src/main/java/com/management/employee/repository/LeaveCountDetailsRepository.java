package com.management.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.LeaveCountDetails;

@Repository
public interface LeaveCountDetailsRepository extends JpaRepository<LeaveCountDetails, Integer> {

	Optional<LeaveCountDetails> findByEmployeeId(Long Id);
	
}
