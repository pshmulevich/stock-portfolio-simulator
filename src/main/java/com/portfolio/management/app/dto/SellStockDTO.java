package com.portfolio.management.app.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellStockDTO {
	
	private List<LotToSellDTO> sharesToSell;
	private double stockPrice;

}
