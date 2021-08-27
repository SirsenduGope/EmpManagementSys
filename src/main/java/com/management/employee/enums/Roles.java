package com.management.employee.enums;

public enum Roles {
	ROLE_USER("User"),
	ROLE_MANAGER("Manager"),
	ROLE_HR("HR"),
	ROLE_ADMIN("Admin");
	
	public final String value;
	
	private Roles(String value) {
		this.value = value;
	}
}
