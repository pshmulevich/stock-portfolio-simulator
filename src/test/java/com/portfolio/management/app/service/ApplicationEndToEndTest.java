package com.portfolio.management.app.service;

import static com.portfolio.management.app.config.jwt.constants.Constants.AUTHORIZATION_HEADER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.portfolio.management.app.dto.BuyStockDTO;
import com.portfolio.management.app.dto.CustomerDTO;
import com.portfolio.management.app.dto.LoginDTO;
import com.portfolio.management.app.dto.LotDTO;
import com.portfolio.management.app.dto.LotToSellDTO;
import com.portfolio.management.app.dto.OwnedStockWithLotsDTO;
import com.portfolio.management.app.dto.SellStockDTO;
import com.portfolio.management.app.dto.SignUpDTO;
import com.portfolio.management.app.entity.Customer;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.CustomerRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;

/**
 * Simulates UI flow and tests it end-to-end. This includes user signup,
 * user login, view portfolio, buy stock, sell stock use case.
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationEndToEndTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OwnedStockRepository ownedStockRepository;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void testEndToEndFlow() {

		String signUpUrl = "/api/portfolio/signup";
		String queryPortfolio = "/api/portfolio/findOwnedStocksWithLotsByPortfolio";
		String buyStockUrl = "/api/portfolio/buyStock";
		String sellStockUrl = "/api/portfolio/sellStock";
		int numSharesToSellPartial = 10;
		String userName = "userName1";
		String firstName = "firstName1";
		String lastName = "lastName1";
		String email = "firstName1@email.com";
		String password = "testPassword";

		assertFalse(customerRepository.findByUserName(userName).isPresent());

		CustomerDTO signUpBody = new CustomerDTO(-1, userName, firstName, lastName, email);

		signUpBody.setPassword(password);
		// Make request to register user
		ResponseEntity<SignUpDTO> signUpResult = testRestTemplate.postForEntity(signUpUrl, signUpBody, SignUpDTO.class);
		//do assertions:
		assertEquals(HttpStatus.OK, signUpResult.getStatusCode());
		// Assert the customer signed up successfully
		assertEquals("User signed up successfully", signUpResult.getBody().getMessage());

		Optional<Customer> userOptional = customerRepository.findByUserName(userName);
		// Assert customer is present
		assertTrue(userOptional.isPresent());
		Customer user = userOptional.get();

		// Assert that the customer's username is "userName1"
		assertEquals(userName, user.getUserName());

		// Assert that the customer's first name is "firstName1"
		assertEquals(firstName, user.getFirstName());

		// Assert that the customer's last name is "lastName1"
		assertEquals(lastName, user.getLastName());		

		// Assert that the customer's email is "firstName1@email.com"
		assertEquals(email, user.getEmail());			

//---------------------------------------------------------------------------------------------------------------------		
		// Make a login request. Get back the token, customerId, portfolioId, accountId
		ResponseEntity<LoginDTO> loginResult = makeLoginRequest(userName, password);

		assertEquals(HttpStatus.OK, loginResult.getStatusCode());
		// verify there is a token issued
		LoginDTO body = loginResult.getBody();
		String token = body.getAuthToken().getToken();
		// Save information from body for useages
		Long portfolioId = body.getPortfolioId();
		Long accountId = body.getAccountId();
		Long customerId = body.getCustomerId();
		assertNotNull(token);
		assertTrue(token.length() > 0);

		ResponseEntity<LoginDTO> wrongPasswordLoginResult = makeLoginRequest(userName, password + "wrong");
		assertEquals(HttpStatus.UNAUTHORIZED, wrongPasswordLoginResult.getStatusCode());

		ResponseEntity<LoginDTO> wrongUserNameLoginResult = makeLoginRequest(userName + "wrong", password);
		assertEquals(HttpStatus.UNAUTHORIZED, wrongUserNameLoginResult.getStatusCode());
		//---------------------------------------------------------------------------------------------------------------------			

		// Make a request to query a portfolio using the token and portfolioId
		String fullPortfolioQuery = queryPortfolio + "/" + portfolioId;
		ResponseEntity<OwnedStockWithLotsDTOResult> portfolioQueryResult = makeAuthenticatedGetRequest(fullPortfolioQuery, token, OwnedStockWithLotsDTOResult.class);
		assertEquals(HttpStatus.OK, portfolioQueryResult.getStatusCode());

		OwnedStockWithLotsDTOResult ownedStockWithLotsDTOList = portfolioQueryResult.getBody();

		assertEquals(0, ownedStockWithLotsDTOList.size());

		// Make a buy request------------------------------------------------------------
		BuyStockDTO buyStockDTO = new BuyStockDTO();
		String stockSymbol = "F";
		int numSharesToBuy = 12;
		double stockPrice = 32.0;
		buyStockDTO.setPortfolioId(portfolioId);	
		buyStockDTO.setStockSymbol(stockSymbol);		
		buyStockDTO.setNumSharesToBuy(numSharesToBuy);		
		buyStockDTO.setStockPrice(stockPrice);
		ResponseEntity<LotDTO> buyStockResult = makeAuthenticatedPostRequest(buyStockUrl, token, buyStockDTO, LotDTO.class);

		// Make sure the person buying the stock is the owner
		long ownerBuyerId = customerRepository.getOne(customerId).getId();
		long buyerId = body.getCustomerId();
		assertEquals(ownerBuyerId, buyerId);
		//do a negative case:
		// 
		long fakeBuyerId = 111L;
		assertNotEquals(fakeBuyerId,buyerId);

		assertEquals(HttpStatus.OK, buyStockResult.getStatusCode());
		//drill down into repositories and verify purchase. Use the LotDTO obj.

		LotDTO buyStockLotDTO = buyStockResult.getBody();

		long ownedStockId = buyStockLotDTO.getOwnedStockDTO().getId();		
		Optional<OwnedStock> testStockOptional = ownedStockRepository.findById(ownedStockId);		
		assertTrue(testStockOptional.isPresent());

		long lotId = buyStockLotDTO.getId(); //(to be used in sell too)

		OwnedStock testStock = testStockOptional.get();
		Portfolio testStockPortfolio = testStock.getPortfolio();
		long testStockPortfolioId = testStockPortfolio.getId();
		assertEquals(testStockPortfolioId, portfolioId);

		//also assert the shares quantity
		int testOwnedShares = buyStockLotDTO.getSharesOwned();
		assertEquals(testOwnedShares, numSharesToBuy);
		//also assert purchase price
		double testOwnedSharesPrice = buyStockLotDTO.getPurchasePrice();
		assertEquals(testOwnedSharesPrice, stockPrice);

		// Make a sell request-----------------------------------------------------------
						
		// First make the lot:
		// LotToSellDTO has 2 params: long lotId and int qty
		LotToSellDTO lotToSellDTO = new LotToSellDTO();		
		lotToSellDTO.setLotId(lotId);
		lotToSellDTO.setQty(numSharesToSellPartial);

		int numExpectedSharesRemaining = (testOwnedShares - numSharesToSellPartial);
		System.out.println("lotToSellDTO.getQty()" + testOwnedShares);
		System.out.println("numSharesToSellPartial" + numSharesToSellPartial);

		// Make the stock:
		// SellStockDTO has 2 params: List<LotToSellDTO> sharesToSell, double stockPrice
		List<LotToSellDTO> sharesToSell = new ArrayList<>();
		sharesToSell.add(lotToSellDTO);

		//sharesToSell.add(lotToSellDTO);
		double stockPriceToSellAt = stockPrice;		
		SellStockDTO sellStockDTO = new SellStockDTO();
		sellStockDTO.setStockPrice(stockPriceToSellAt);
		sellStockDTO.setSharesToSell(sharesToSell);
		// use sellStockDTO for REST call
		ResponseEntity<Void> sellStockResult = makeAuthenticatedPostRequest(sellStockUrl, token, sellStockDTO, Void.class);

		assertEquals(HttpStatus.OK, sellStockResult.getStatusCode());

		//partial shares, expecting 2 remaining shares
		assertEquals(numExpectedSharesRemaining, 2);
	}
	//=================================================================================
	//=================================================================================

	@Test
	void testEndToEndFlowFlawedPurchase() {
		assertTrue(customerRepository.count()==0);

		String signUpUrl = "/api/portfolio/signup";
		String queryPortfolio = "/api/portfolio/findOwnedStocksWithLotsByPortfolio";
		String buyStockUrl = "/api/portfolio/buyStock";
		String sellStockUrl = "/api/portfolio/sellStock";
		int numSharesToSellGreater = 45;
		String userName = "userName2";
		String firstName = "firstName2";
		String lastName = "lastName2";
		String email = "firstName2@email.com";
		String password = "testPassword2";

		CustomerDTO signUpBody = new CustomerDTO(-1, userName, firstName, lastName, email);

		signUpBody.setPassword(password);
		ResponseEntity<SignUpDTO> signUpResult = testRestTemplate.postForEntity(signUpUrl, signUpBody, SignUpDTO.class);
		//do assertions:
		assertEquals(HttpStatus.OK, signUpResult.getStatusCode());
		assertEquals("User signed up successfully", signUpResult.getBody().getMessage());

		Optional<Customer> userOptional = customerRepository.findByUserName(userName);
		assertTrue(userOptional.isPresent());
		Customer user = userOptional.get();		

		//----------------------------------------------------	
		// Make a login request. Get back the token, customerId, portfolioId, accountId
		ResponseEntity<LoginDTO> loginResult = makeLoginRequest(userName, password);
		assertEquals(HttpStatus.OK, loginResult.getStatusCode());
		// verify there is a token issued
		LoginDTO body = loginResult.getBody();
		String token = body.getAuthToken().getToken();
		// Save information from body for useages
		Long portfolioId = body.getPortfolioId();
		Long customerId = body.getCustomerId();
		assertNotNull(token);
		assertTrue(token.length() > 0);
		//---------------------------------------------------------------------------------------------------------------------			
		// Make a request to query a portfolio using the token and portfolioId
		String fullPortfolioQuery = queryPortfolio + "/" + portfolioId;
		ResponseEntity<OwnedStockWithLotsDTOResult> portfolioQueryResult = makeAuthenticatedGetRequest(fullPortfolioQuery, token, OwnedStockWithLotsDTOResult.class);
		assertEquals(HttpStatus.OK, portfolioQueryResult.getStatusCode());

		// Make a buy request-------buy 12 shares for this test--------------------------------------------
		BuyStockDTO buyStockDTO = new BuyStockDTO();
		String stockSymbol = "F";
		int numSharesToBuy = 12;
		double stockPrice = 32.0;
		buyStockDTO.setPortfolioId(portfolioId);	
		buyStockDTO.setStockSymbol(stockSymbol);		
		buyStockDTO.setNumSharesToBuy(numSharesToBuy);		
		buyStockDTO.setStockPrice(stockPrice);
		ResponseEntity<LotDTO> buyStockResult = makeAuthenticatedPostRequest(buyStockUrl, token, buyStockDTO, LotDTO.class);
		assertEquals(HttpStatus.OK, buyStockResult.getStatusCode());
		//drill down into repositories and verify purchase. Use the LotDTO obj.

		LotDTO buyStockLotDTO = buyStockResult.getBody();		

		long lotId = buyStockLotDTO.getId(); //(to be used in sell too)

		//also assert the shares quantity
		int testOwnedShares = buyStockLotDTO.getSharesOwned();
		assertEquals(testOwnedShares, numSharesToBuy);
		//also assert purchase price
		double testOwnedSharesPrice = buyStockLotDTO.getPurchasePrice();
		assertEquals(testOwnedSharesPrice, stockPrice);

		// Create sell request------Sell more than the 12 shares we have---------------------------------						
		// First create the lot:
		LotToSellDTO lotToSellDTO = new LotToSellDTO();		
		lotToSellDTO.setLotId(lotId);
		lotToSellDTO.setQty(numSharesToSellGreater);

		// Create a list of lots:
		List<LotToSellDTO> sharesToSell = new ArrayList<>();
		sharesToSell.add(lotToSellDTO);

		double stockPriceToSellAt = stockPrice;		
		SellStockDTO sellStockDTO = new SellStockDTO();
		sellStockDTO.setStockPrice(stockPriceToSellAt);
		sellStockDTO.setSharesToSell(sharesToSell);

		ResponseEntity<Void> sellStockResult = makeAuthenticatedPostRequest(sellStockUrl, token, sellStockDTO, Void.class);
		// Expecting internal service error http code 500
		// TODO: service needs to provide a more meaningful error message
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, sellStockResult.getStatusCode());
	}

	//===================================================
	private <T> ResponseEntity<T> makeAuthenticatedGetRequest(String url, String token, Class<T> returnDataType) {
		HttpHeaders headers = new HttpHeaders();				
		headers.add(AUTHORIZATION_HEADER_NAME, "Bearer " + token);
		return testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), returnDataType);
	}

	private <T, V> ResponseEntity<T> makeAuthenticatedPostRequest(String url, String token, V requestBody, Class<T> returnDataType) {
		HttpHeaders headers = new HttpHeaders();				
		headers.add(AUTHORIZATION_HEADER_NAME, "Bearer " + token);
		return testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<V>(requestBody, headers), returnDataType);
	}

	private ResponseEntity<LoginDTO> makeLoginRequest(String userName, String password) {
		String loginUrl = "/api/portfolio/token";
		CustomerDTO loginBody = new CustomerDTO();
		loginBody.setUserName(userName);
		loginBody.setPassword(password);
		ResponseEntity<LoginDTO> loginResult = testRestTemplate.postForEntity(loginUrl, loginBody, LoginDTO.class);
		return loginResult;
	}

	// TODO: refactor this repeating class definition
	// Using this as a response entity type to work around warnings and generic type limitations.
	static class OwnedStockWithLotsDTOResult extends ArrayList<OwnedStockWithLotsDTO> {
		private static final long serialVersionUID = 5134786708673475724L;
	}

}
