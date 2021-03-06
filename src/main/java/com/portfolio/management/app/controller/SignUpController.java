package com.portfolio.management.app.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.management.app.config.jwt.JwtTokenUtil;
import com.portfolio.management.app.config.jwt.dto.AuthToken;
import com.portfolio.management.app.dto.AccountDTO;
import com.portfolio.management.app.dto.CustomerDTO;
import com.portfolio.management.app.dto.LoginDTO;
import com.portfolio.management.app.dto.SignUpDTO;
import com.portfolio.management.app.entity.Account;
import com.portfolio.management.app.entity.Customer;
import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.AccountRepository;
import com.portfolio.management.app.repository.CustomerRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Controller for signing up new customers over REST API
 *
 */
@RestController
@RequestMapping("/api/portfolio")
@Api(tags = "Registration and sign-in API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SignUpController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private OwnedStockRepository ownedStockRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
	/**
	 * This is a debug method
	 */
	@GetMapping("/demoSignup")
	@ApiIgnore("This endpoint will be deprecated")
	@ApiOperation(
            value = "Registration simulation",
            notes = "Simulate a user Registering an account.")
	public String demoSignup() {
		
		signUpDemoCustomer("jsmithbigbucks", "John", "Smith", "JSmith@myob.com");
		signUpDemoCustomer("donttellmyhusband", "Kerrie", "Smith", "KSmith@myob.com");
		signUpDemoCustomer("hiddencash", "Jessica", "Smith", "JSmith@myob.com");
		
		return "Signed up demo customers";
	}
	
	@PostMapping("/signup")
	@ApiOperation(
            value = "Register a new customer",
            notes = "Register a new customer.")
	public ResponseEntity<SignUpDTO> signup(@RequestBody CustomerDTO customerDTO) {
		String userName = customerDTO.getUserName();
		String firstName = customerDTO.getFirstName();
		String lastName = customerDTO.getLastName();
		String email = customerDTO.getEmail();
		String password = customerDTO.getPassword();
		
		// Verify userName and email do not exist
		Optional<Customer> existingUserName = customerRepository.findByUserName(userName);
		if(existingUserName.isPresent()) {
			return new ResponseEntity<>(new SignUpDTO("Username already exists"), HttpStatus.CONFLICT);		
		}
		
		Optional<Customer> existingEmail = customerRepository.findByEmail(email);
		if(existingEmail.isPresent()) {
			return new ResponseEntity<>(new SignUpDTO("Email already exists"), HttpStatus.CONFLICT);		
		}

		Portfolio portfolio = new Portfolio("Investment Portfolio");
		LocalDate date = LocalDate.now();
		Account account = new Account("Personal Account", date, portfolio);

		String encodedPassword = passwordEncoder.encode(password);
		Customer customer = new Customer(userName, firstName, lastName, email, encodedPassword , account);
		customerRepository.save(customer);
		SignUpDTO signUpDTO = new SignUpDTO("User signed up successfully");
		return new ResponseEntity<>(signUpDTO, HttpStatus.OK);		
	}

	@PostMapping("/token")
	@ApiOperation(
            value = "Sign in and retrieve JWT Authrization token",
            notes = "Retrieve a token after a user registers.")
    public ResponseEntity<LoginDTO> issueAuthToken(@RequestBody CustomerDTO customerDTO) throws AuthenticationException {
		String userName = customerDTO.getUserName();
		String password = customerDTO.getPassword();
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		} catch (AuthenticationException e) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		final String token = jwtTokenUtil.generateToken(userDetails);
		AuthToken authToken = new AuthToken(token, userDetails.getUsername());

		Optional<Customer> customerOptional = customerRepository.findByUserName(userName);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			// TODO: These two sections need to be refactored
			Set<Account> accounts = customer.getAccounts();
			// See: https://mkyong.com/java/java-how-to-get-the-first-item-from-set/
			Optional<Account> accountOptional = accounts.stream().findFirst();
			// Assuming we have one and only one account
			Assert.isTrue(accountOptional.isPresent(),
					"Customer must have at least one account. Customer username: " + userName);
			Account account = accountOptional.get();

			Set<Portfolio> portfolios = account.getPortfolios();
			Optional<Portfolio> portfolioOptional = portfolios.stream().findFirst();
			// Assuming we have one and only one portfolio
			Assert.isTrue(portfolioOptional.isPresent(),
					"Account must have at least one portfolio. Account Id: " + account.getId());
			Portfolio portfolio = portfolioOptional.get();
			LoginDTO loginDTO = new LoginDTO(customer.getId(), account.getId(), portfolio.getId(), authToken);
			return new ResponseEntity<>(loginDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
    }
	
	@GetMapping("/findAllCustomers")
	@ApiIgnore("This endpoint will be deprecated")
	@ApiOperation(
            value = "Retrieve customers",
            notes = "Retrieve all customers that are registered.")
	public List<CustomerDTO> findAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		List<CustomerDTO> customerDTOs = new ArrayList<>();
		for(Customer customer : customers) {
			customerDTOs.add(toCustomerDTO(customer));
		}
		return customerDTOs;		
	}
	
	@GetMapping("/findAccountsByCustomer/{customerId}")
	@ApiIgnore("This endpoint will be deprecated")
	@ApiOperation(
            value = "Find accounts by customer ID",
            notes = "Find all accounts that are owned by a customer ID.")
	@ApiImplicitParam(name = "Authorization", required = true, paramType = "header",
		dataType = "string", value = "authorization header", defaultValue = "Bearer ")	
	public ResponseEntity<List<AccountDTO>> findAccountsByCustomer(@PathVariable long customerId) {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		List<AccountDTO> accountDTOs = new ArrayList<>();
		if(customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			List<Account> accounts = accountRepository.findByCustomer(customer);
			
			for(Account account : accounts) {
				long id = account.getId();
				String name = account.getName();
				LocalDate dateOpened = account.getDateOpened();				
				accountDTOs.add(new AccountDTO(id, name, dateOpened, toCustomerDTO(customer)));
			}
			return new ResponseEntity<>(accountDTOs, HttpStatus.OK);
		} else {
			// TODO: need to be able to provide custom error message
			return new ResponseEntity<>(accountDTOs, HttpStatus.NOT_FOUND);
		}
	}

	private void signUpDemoCustomer(String userName, String firstName, String lastName, String email) {
		Portfolio portfolio = new Portfolio("Investment Portfolio");
		LocalDate date = LocalDate.now();
		Account account = new Account("Personal Account", date, portfolio);
		

		String password = "password";
		Customer customer = new Customer(userName, firstName, lastName, email, password, account);
		customerRepository.save(customer);		
    	
		ownedStockRepository.saveAll(Arrays.asList(
    			new OwnedStock("AAPL", portfolio , new Lot(10, 300.0), new Lot(50, 314.0)),
    			new OwnedStock("CRM", portfolio, new Lot(40, 230.0), new Lot(20, 426.0))
			));
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
