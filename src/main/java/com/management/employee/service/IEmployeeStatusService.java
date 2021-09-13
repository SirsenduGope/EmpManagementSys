package com.management.employee.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.management.employee.entity.EmployeeStatus;

public interface IEmployeeStatusService {

	public ResponseEntity<?> getAllStatus() throws Exception;

	public ResponseEntity<?> updateEmployeeStatus(EmployeeStatus employeeStatus) throws Exception;

	public ResponseEntity<?> deleteEmployeeStatusById(String id) throws Exception;

	public ResponseEntity<?> addNewEmployeeStauts(EmployeeStatus newEmployeeStatus) throws Exception;

	public Optional<EmployeeStatus> getEmployeeStatusByName(String empStatus) throws Exception;


}
