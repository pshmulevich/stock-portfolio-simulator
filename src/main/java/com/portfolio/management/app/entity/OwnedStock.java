package com.portfolio.management.app.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ownedStock")
@Getter
@Setter
public class OwnedStock implements Serializable {

	private static final long serialVersionUID = 4408989578530571021L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "stockSymbol")
	private String stockSymbol;

	@OneToMany(mappedBy = "ownedStock", cascade = CascadeType.ALL)
    private Set<Lot> lots;
	
	// Must provide default constructor because java would not do that since there is another constructor
	public OwnedStock() {
	}
	
    public OwnedStock(String stockSymbol, Lot... lots) {
        this.stockSymbol = stockSymbol;
        this.lots = Stream.of(lots).collect(Collectors.toSet());
        this.lots.forEach(lot -> lot.setOwnedStock(this));
    }
}
