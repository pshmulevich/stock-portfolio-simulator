package com.portfolio.management.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.portfolio.management.app.entity.Account;
import com.portfolio.management.app.entity.Customer;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.AccountRepository;
import com.portfolio.management.app.repository.CustomerRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;
import com.portfolio.management.app.repository.PortfolioRepository;

@Service
public class UserContextService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private PortfolioRepository portfolioRepository;
	
	@Autowired
	private OwnedStockRepository ownedStockRepository;
	
	public boolean isCustomerIdValidForContext(Long customerId) {
		Optional<Customer> repositoryCustomerOptional = customerRepository.findById(customerId);
		Assert.isTrue(repositoryCustomerOptional.isPresent(), "Portfolio must exist: " + customerId);
		Customer repositoryCustomer = repositoryCustomerOptional.get();

		String customerName = getLoggedInUser();
		// Then compare
		return repositoryCustomer.getUserName().equals(customerName);
	}

	public boolean isAccountIdValidFromContext(long accountId) {
		Optional<Account> repositoryAccountOptional = accountRepository.findById(accountId);
		Assert.isTrue(repositoryAccountOptional.isPresent(), "Account must exist: " + accountId);
		Account repositoryAccount = repositoryAccountOptional.get();
		
		Customer customer = repositoryAccount.getCustomer();
		return isCustomerLoggedIn(customer);
	}

	public boolean isOwnedStockIdValidFromContext(long ownedStockId) {
		Optional<OwnedStock> repositoryOwnedStockOptional = ownedStockRepository.findById(ownedStockId);
		Assert.isTrue(repositoryOwnedStockOptional.isPresent(), "Owned stock must exist: " + ownedStockId);
		OwnedStock repositoryOwnedStock = repositoryOwnedStockOptional.get();
		
		Customer customer = repositoryOwnedStock.getPortfolio().getAccount().getCustomer();
		return isCustomerLoggedIn(customer);
	}

	public boolean isPortfolioIdValidFromContext(Long portfolioId) {
		Optional<Portfolio> repositoryPortfolioOptional = portfolioRepository.findById(portfolioId);
		Assert.isTrue(repositoryPortfolioOptional.isPresent(), "Portfolio must exist: " + portfolioId);
		Portfolio repositoryPortfolio = repositoryPortfolioOptional.get();
		
		Account account = repositoryPortfolio.getAccount();
		Customer customer = account.getCustomer();
		return isCustomerLoggedIn(customer);
	}

	public String getLoggedInUser() {
		Authentication authenticationFromContext = SecurityContextHolder.getContext().getAuthentication();
		String customerName = authenticationFromContext.getName();
		return customerName;
	}

	private boolean isCustomerLoggedIn(Customer customer) {
		String customerName = customer.getUserName();
		String customerNameFromContext = getLoggedInUser();		
		return customerName.equals(customerNameFromContext);
	}


//	public boolean isOwnedStockIdValidFromContext(Long ownedStockId) {
//		Authentication authenticationFromContext = SecurityContextHolder.getContext().getAuthentication();
//		// Account has Customer, has portfolios, has lots, has owned stock
//		
//		return false;
//
//	}
}
