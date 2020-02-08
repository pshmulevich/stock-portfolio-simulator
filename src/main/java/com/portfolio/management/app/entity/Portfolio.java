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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "portfolio")
@Getter
@Setter
public class Portfolio implements Serializable {

	private static final long serialVersionUID = -3748496235145115928L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "name")
	private String name;
	
    @ManyToOne
    @JoinColumn
    @NotNull
	private Account account;
	
	@OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private Set<OwnedStock> ownedStocks;
	
	// Must provide default constructor because java would not do that since there is another constructor
	public Portfolio() {
	}
	
	public Portfolio(String name, OwnedStock... ownedStocks) {
		this.name = name;
		
        this.ownedStocks = Stream.of(ownedStocks).collect(Collectors.toSet());
        this.ownedStocks.forEach(ownedStock -> ownedStock.setPortfolio(this));
	}
}
