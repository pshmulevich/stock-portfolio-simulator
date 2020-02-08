package com.portfolio.management.app.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.management.app.dto.AccountDTO;
import com.portfolio.management.app.dto.CustomerDTO;
import com.portfolio.management.app.entity.Account;
import com.portfolio.management.app.entity.Customer;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.AccountRepository;
import com.portfolio.management.app.repository.CustomerRepository;

/**
 * Controller for signing up new customers over REST API
 *
 */
@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SignUpController {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AccountRepository accountRepository;
	
	/**
	 * This is a debug method
	 */
	@GetMapping("/testSignup")
	public String testSignup() {

		Portfolio portfolio = new Portfolio("Investment Portfolio");
		LocalDate date = LocalDate.now();
		Account account = new Account("Personal Account", date, portfolio);
		
		String userName = "customer1";
		String firstName = "John";
		String lastName = "Smith";
		String email = "jsmith@myob.com";
		String password = "password";
		Customer customer = new Customer(userName, firstName, lastName, email, password, account);
		customerRepository.save(customer);		
		
		return "Customer signed up";
	}
	
	@PostMapping("/signup")
	public String signup(@RequestBody CustomerDTO customerDTO) {

		Portfolio portfolio = new Portfolio("Investment Portfolio");
		LocalDate date = LocalDate.now();
		Account account = new Account("Personal Account", date, portfolio);
		
		String userName = customerDTO.getUserName();
		String firstName = customerDTO.getFirstName();
		String lastName = customerDTO.getLastName();
		String email = customerDTO.getEmail();
		String password = customerDTO.getPassword();
		Customer customer = new Customer(userName, firstName, lastName, email, password, account);
		customerRepository.save(customer);
		
		return "Customer signed up";		
	}
	
	@GetMapping("/findAllCustomers")
	public List<CustomerDTO> findAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		List<CustomerDTO> customerDTOs = new ArrayList<>();
		for(Customer customer : customers) {
			customerDTOs.add(toCustomerDTO(customer));
		}
		return customerDTOs;		
	}
	
	@GetMapping("/findAllAccountsByCustomer/{customerId}")
	public List<AccountDTO> findAllAccountsByCustomer(@PathVariable long customerId) {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		List<AccountDTO> accountDTOs = new ArrayList<>();
		if(customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			List<Account> accounts = accountRepository.findByCustomer(customer);
			
			for(Account account : accounts) {
				String name = account.getName();
				LocalDate dateOpened = account.getDateOpened();				
				accountDTOs.add(new AccountDTO(name, dateOpened, toCustomerDTO(customer)));
			}
		}
		return accountDTOs;
	}

	private CustomerDTO toCustomerDTO(Customer customer) {
		long id = customer.getId();
		String userName = customer.getUserName();
		String firstName = customer.getFirstName();
		String lastName = customer.getLastName();
		String email = customer.getEmail();
		CustomerDTO customerDTO = new CustomerDTO(id, userName, firstName, lastName, email);
		return customerDTO;
	}
}
