package com.portfolio.management.app.dto;

public class OwnedStockDTO {
	private long id;
	private String stockSymbol;
	
	public OwnedStockDTO(long id, String stockSymbol) {
		this.id = id;
		this.stockSymbol = stockSymbol;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	
}
