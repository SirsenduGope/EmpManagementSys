package com.management.employee.enums;

import java.util.HashMap;
import java.util.Map;

public enum Roles {
	ROLE_USER("user"),
	ROLE_MANAGER("manager"),
	ROLE_HR("hr"),
	ROLE_ADMIN("admin");
	
	public final String value;
	
	private static final Map<String, Roles> lookup = new HashMap<String, Roles>();
	
	static {
        for (Roles r : Roles.values()) {
            lookup.put(r.getValue(), r);
        }
    }
	
	private Roles(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
	
	public static Roles get(String value) {
        return lookup.get(value);
    }
}
