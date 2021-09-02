package com.management.employee.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.management.employee.enums.Roles;

@Entity
@Table(name = "roles")
@SQLDelete(sql = "UPDATE employees SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Role extends Auditable<String> implements Serializable {
	
	private static final long serialVersionUID = 7845511262239119877L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "Role", length = 50)
	private Roles role;
	
	@ManyToMany(mappedBy = "roles")
	private Set<Employee> employees;
	
	@JsonIgnore
	@Column(name = "deleted", columnDefinition = "bit(1) default b'0'")
	private boolean deleted = Boolean.FALSE;

	public Role() {
		super();
	}

	public Role(Roles role, Set<Employee> employees) {
		super();
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

		
}
