package com.management.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	@Query("select e from Employee e where e.email = ?1")
	Optional<Employee> findByEmail(String email);
	
	Boolean existsByEmail(String email);
	
	@Query(value = "select * from employees e where e.report_to = ?1 and e.deleted = 0", nativeQuery = true)
	Optional<List<Employee>> findByReportTo(String email);
	
	@Query(value = "select * from employees e where e.id in (select employee_id from employee_roles where role_id = ?1)", nativeQuery = true)
	Optional<List<Employee>> findByRoleId(Integer id);
}
