package com.portfolio.management.app.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PortfolioDTO {

	private long id;
	private String name;
	
	public PortfolioDTO(long id, String name) {
		this.id = id;
		this.name = name;
	}
}
