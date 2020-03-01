package com.portfolio.management.app.dto;

import com.portfolio.management.app.config.jwt.dto.AuthToken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
	private long customerId;
	private long accountId;
	private long portfolioId;
	private String message;
	private AuthToken authToken;
	
	public LoginDTO(String message) {
		this.message = message;
	}
	
	public LoginDTO(long customerId, long accountId, long portfolioId, AuthToken authToken) {
		this.customerId = customerId;
		this.accountId = accountId;
		this.portfolioId = portfolioId;
		this.authToken = authToken;
	}
}
