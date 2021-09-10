package com.management.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.OrganizationDetails;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationDetails, Integer> {

	
}
