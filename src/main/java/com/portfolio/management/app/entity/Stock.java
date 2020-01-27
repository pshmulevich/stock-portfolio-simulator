package com.portfolio.management.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "stock")
@Getter
@Setter
public class Stock implements Serializable {

	private static final long serialVersionUID = -2343243243242432341L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "stockSymbol")
	private String stockSymbol;
	
	@Column(name = "openValue")
	private Double openValue;
	
	@Column(name = "dayRange")
	private Double dayRange;
	
	@Column(name = "dividendRate")
	private Double dividendRate;
	
	@Column(name = "peRatio")
	private Double peRatio;
	
	@Column(name = "previousClose")
	private Double previousClose;
	
	@Column(name = "volume")
	private int volume;

	// Must provide default constructor because java would not do that since there is another constructor
	public Stock() {
	}

	public Stock(String stockSymbol, Double openValue, Double dayRange, Double dividendRate, Double peRatio, Double previousClose, int volume) {
		this.stockSymbol = stockSymbol;
		this.openValue = openValue;
		this.dayRange = dayRange;
		this.dividendRate = dividendRate;
		this.peRatio = peRatio;
		this.previousClose = previousClose;
		this.volume = volume;
		
	}
}
