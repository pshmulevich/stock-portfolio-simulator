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
@Table(name = "customer")
@Getter
@Setter
public class Customer implements Serializable {

	private static final long serialVersionUID = -6294012404840837167L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "userName")
	private String userName;
	
	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name = "email")
	private String email;
		
	// TODO: need to implement encryption 
	// see: https://thoughts-on-java.org/jpa-21-how-to-implement-type-converter/
	// see: https://blog.arnoldgalovics.com/encrypting-jpa-entity-attributes-using-listeners-in-spring/
	@Column(name = "password")
	private String password;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<Account> accounts;
	
	
	// Must provide default constructor because java would not do that since there is another constructor
	public Customer() {
	}

	public Customer(String userName, String firstName, String lastName, String email, String password, Account... accounts) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		
        this.accounts = Stream.of(accounts).collect(Collectors.toSet());
        this.accounts.forEach(account -> account.setCustomer(this));
	}
}
