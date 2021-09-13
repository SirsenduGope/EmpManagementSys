package com.management.employee.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Helper {
	
	private static final Logger logger = LoggerFactory.getLogger(Helper.class);
	
	public static String loggedInUserEmailId() {
		UserDetails loggedInUserDetails =
				(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return loggedInUserDetails.getUsername();
	}
	
	public static String loggedInUserAuthority() {
		UserDetails loggedInUserDetails =
				(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return loggedInUserDetails.getAuthorities().toArray()[0].toString();
	}
	
	public static int getTotalLeavesByCalculatingFromDateToDate(Date fromDay, Date toDay) {
		int totalLeaveDays = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date fromDate = sdf.parse(fromDay.toString());
			Date toDate = sdf.parse(toDay.toString());
			totalLeaveDays = (int) ((Math.abs(toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24)) % 365);
		}catch (ParseException ex) {
			logger.debug("ERROR : Parse exception from requestForLeave method : " + ex.getMessage());
            ex.printStackTrace();
        }
		
		return totalLeaveDays;
		
	}

}
