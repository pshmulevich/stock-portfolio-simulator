package com.portfolio.management.app.dto;

public class StockDTO {

	private String stockSymbol;
	private Double openValue;
	private Double dayRange;
	private Double dividendRate;
	private Double peRatio;
	private Double previousClose;
	int volume;
	
	public StockDTO(String stockSymbol, Double openValue, Double dayRange, Double dividendRate, Double peRatio, Double previousClose, int volume) {
		this.stockSymbol = stockSymbol;
		this.openValue = openValue;
		this.dayRange = dayRange;
		this.dividendRate = dividendRate;
		this.peRatio = peRatio;
		this.previousClose = previousClose;
		this.volume = volume;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public Double getOpenValue() {
		return openValue;
	}

	public void setOpenValue(Double openValue) {
		this.openValue = openValue;
	}

	public Double getDayRange() {
		return dayRange;
	}

	public void setDayRange(Double dayRange) {
		this.dayRange = dayRange;
	}

	public Double getDividendRate() {
		return dividendRate;
	}

	public void setDividendRate(Double dividendRate) {
		this.dividendRate = dividendRate;
	}

	public Double getPeRatio() {
		return peRatio;
	}

	public void setPeRatio(Double peRatio) {
		this.peRatio = peRatio;
	}

	public Double getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(Double previousClose) {
		this.previousClose = previousClose;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}	
}
