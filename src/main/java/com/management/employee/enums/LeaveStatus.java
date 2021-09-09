package com.management.employee.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeaveStatus {
	ACCEPTED("accepted"),
	REJECTED("rejected"),
	REQUESTED("requested");
	
	public final String value;
	
	private static final Map<String, LeaveStatus> lookup = new HashMap<String, LeaveStatus>();
	
	static {
        for (LeaveStatus r : LeaveStatus.values()) {
            lookup.put(r.getValue(), r);
        }
    }
	
	private LeaveStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
	
	public static LeaveStatus get(String value) {
        return lookup.get(value);
    }
}
