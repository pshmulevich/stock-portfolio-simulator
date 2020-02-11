package com.portfolio.management.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {
	private String message;
	
	public SignUpDTO(String message) {
		this.message = message;
	}
}
