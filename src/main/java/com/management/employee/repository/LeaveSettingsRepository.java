package com.management.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.LeaveSettings;

@Repository
public interface LeaveSettingsRepository extends JpaRepository<LeaveSettings, Integer> {

	
}
