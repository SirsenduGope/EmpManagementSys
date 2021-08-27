package com.management.employee;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.management.employee.entity.Employee;

public class CustomUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7084876435539928229L;
	private Employee employee;
	private Collection<? extends GrantedAuthority> authorities;
	
	public CustomUserDetails(Employee employee,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.employee = employee;
		this.authorities = authorities;
	}
	
	public static CustomUserDetails build(Employee employee) {
		List<GrantedAuthority> authorities = employee.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole().name()))
				.collect(Collectors.toList());

		return new CustomUserDetails(
				employee,
				authorities);
	}
	
	public Employee getEmployee() {
		return employee;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return employee.getPassword();
	}

	@Override
	public String getUsername() {
		return employee.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
//		CustomUserDetails user = (CustomUserDetails) o;
//		return Objects.equals(id, user.id);
//	}
	
}
