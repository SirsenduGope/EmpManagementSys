package com.management.employee.payload;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.employee.entity.Views;

public class Message {
	
	@JsonView(Views.Public.class)
	private String messsage;
	
	public Message(String messsage) {
		super();
		this.messsage = messsage;
	}

	public String getMesssage() {
		return messsage;
	}

	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}
	
	

}
