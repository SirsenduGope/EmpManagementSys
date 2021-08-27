package com.management.employee.enums;

public enum Gender {
	MALE("Male"),
	FEMALE("Female"),
	OTHERS("Others");
	
	public final String value;
	
	private Gender(String value) {
		this.value = value;
	}
}
