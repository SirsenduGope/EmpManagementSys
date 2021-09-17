package com.management.employee.enums;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
	MALE("Male"),
	FEMALE("Female"),
	OTHERS("Others");
	
	public final String value;
	
	private static final Map<String, Gender> lookup = new HashMap<String, Gender>();
	
	static {
        for (Gender r : Gender.values()) {
            lookup.put(r.getValue(), r);
        }
    }
	
	private Gender(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
	
	public static Gender get(String value) {
        return lookup.get(value);
    }
}
