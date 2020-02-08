package com.portfolio.management.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "account")
@Getter
@Setter
public class Account implements Serializable {

	private static final long serialVersionUID = -6294012404840837167L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "dateOpened")
	private LocalDate dateOpened;

    @ManyToOne
    @JoinColumn
    @NotNull
	private Customer customer;
    
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private Set<Portfolio> portfolios;
	
	// Must provide default constructor because java would not do that since there is another constructor
	public Account() {
	}

	public Account(String name, LocalDate dateOpened, Portfolio... portfolios) {
		this.name = name;
		this.dateOpened = dateOpened;
		
        this.portfolios = Stream.of(portfolios).collect(Collectors.toSet());
        this.portfolios.forEach(portfolio -> portfolio.setAccount(this));
	}
}
