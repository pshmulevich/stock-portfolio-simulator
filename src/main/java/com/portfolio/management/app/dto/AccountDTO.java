package com.portfolio.management.app.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
	private long id;
	private String name;
	private LocalDate dateOpened;
	private CustomerDTO customer;
	
	public AccountDTO() {
		
	}
	public AccountDTO(long id, String name, LocalDate dateOpened, CustomerDTO customer) {
		this.id = id;
		this.name = name;
		this.dateOpened = dateOpened;
		this.customer = customer;
	}
}
