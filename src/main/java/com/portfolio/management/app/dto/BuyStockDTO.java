package com.portfolio.management.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyStockDTO {
	
	private long portfolioId;
	private String stockSymbol;
	private int numSharesToBuy;
	private double stockPrice;

}
