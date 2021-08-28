package com.management.employee.utils;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Helper {
	
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

}
