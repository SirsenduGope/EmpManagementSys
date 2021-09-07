package com.management.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.EmployeeStatus;

@Repository
public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Integer> {
	
	Optional<EmployeeStatus> findByStatus(String status);

	Boolean existsByStatus(String status);

}
