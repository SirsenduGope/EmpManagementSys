package com.management.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.EmployeeDetails;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {
	
	Optional<List<EmployeeDetails>> findByDesignationId(Integer id);

	Optional<List<EmployeeDetails>> findByEmpStatusId(int parseInt);

}
