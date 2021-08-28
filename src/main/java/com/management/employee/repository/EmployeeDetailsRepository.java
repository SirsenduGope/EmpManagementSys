package com.management.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.employee.entity.EmployeeDetails;

public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {

}
