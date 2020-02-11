package com.portfolio.management.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
	private long customerId;
	private long accountId;
	private long portfolioId;
	private String message;
	
	public LoginDTO(String message) {
		this.message = message;
	}
	
	public LoginDTO(long customerId, long accountId, long portfolioId) {
		this.customerId = customerId;
		this.accountId = accountId;
		this.portfolioId = portfolioId; 
	}
}
