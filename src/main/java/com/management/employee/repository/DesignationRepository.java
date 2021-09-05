package com.management.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {

	Optional<Designation> findByDesignation(String designation);
}
