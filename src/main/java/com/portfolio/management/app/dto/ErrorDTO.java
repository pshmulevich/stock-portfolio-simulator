package com.portfolio.management.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
	public ErrorDTO(String message) {
		this.message = message;
	}

	private String message;
}
