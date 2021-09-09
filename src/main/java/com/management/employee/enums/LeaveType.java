package com.management.employee.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeaveType {
	CASUAL_LEAVE("casual_leave"),
	SICK_LEAVE("sick_leave"),
	EARN_LEAVE("earn_leave");
	
	public final String value;
	
	private static final Map<String, LeaveType> lookup = new HashMap<String, LeaveType>();
	
	static {
        for (LeaveType r : LeaveType.values()) {
            lookup.put(r.getValue(), r);
        }
    }
	
	private LeaveType(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
	
	public static LeaveType get(String value) {
        return lookup.get(value);
    }
}
