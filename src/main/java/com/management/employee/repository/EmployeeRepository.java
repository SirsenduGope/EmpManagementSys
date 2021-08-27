package com.management.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	@Query("select e from Employee e where e.email = ?1")
	Employee findByEmail(String email);
	
	Boolean existsByEmail(String email);
}
