package com.portfolio.management.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
	private long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	public CustomerDTO() {		
	}
	
	public CustomerDTO(long id, String userName, String firstName, String lastName, String email) {
		this.id = id;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}	
}
