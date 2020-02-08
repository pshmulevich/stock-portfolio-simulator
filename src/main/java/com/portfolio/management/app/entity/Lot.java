package com.portfolio.management.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lot")
@Getter
@Setter
public class Lot implements Serializable {
	
	private static final long serialVersionUID = -4504629339911562336L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "sharesOwned")
	private int sharesOwned;
	
	@Column(name = "purchasePrice")
	private double purchasePrice;

    @ManyToOne
    @JoinColumn
    @NotNull
	private OwnedStock ownedStock;
	
	// Must provide default constructor because java would not do that since there is another constructor
	public Lot() {
	}

	public Lot(int sharesOwned, double purchasePrice) {
		this.sharesOwned = sharesOwned;
		this.purchasePrice = purchasePrice;
		
	}
}
