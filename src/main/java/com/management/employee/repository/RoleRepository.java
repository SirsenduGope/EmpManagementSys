package com.management.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.management.employee.entity.Role;
import com.management.employee.enums.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRole(Roles name);
	
	@Query(value = "select * from roles where role = ?1", nativeQuery = true)
	Optional<Role> findByName(String roleName);
}
