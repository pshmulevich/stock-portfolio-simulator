package com.portfolio.management.app.controller;

import static com.portfolio.management.app.config.jwt.constants.Constants.AUTHORIZATION_HEADER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.portfolio.management.app.config.jwt.JwtTokenUtil;
import com.portfolio.management.app.dto.OwnedStockDTO;
import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.LotRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;
import com.portfolio.management.app.repository.PortfolioRepository;
import com.portfolio.management.app.service.UserContextService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class PortfolioControllerTest {

	@MockBean
	private LotRepository lotRepository;

	@MockBean
	private OwnedStockRepository ownedStockRepository;

	@MockBean
	private PortfolioRepository portfolioRepository;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	private UserDetailsService userDetailsService;
	
	@MockBean
	private UserContextService userContextService;

	private UserDetails userDetails = Mockito.mock(UserDetails.class);
	
	/**
	 * Test finding stocks using specific portfolio Id
	 */
	@Test
	void findOwnedStocksByPortfolio() {
		
		Portfolio portfolio = Mockito.mock(Portfolio.class);
		String testUserName = "TestUser1";
		long portfolioId = 10L;
		when(portfolio.getId()).thenReturn(portfolioId );
		when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
		
		List<OwnedStock> ownedStocks = Arrays.asList(
				new OwnedStock("AAPL", portfolio, new Lot(10, 300.0), new Lot(50, 314.0)),
				new OwnedStock("CRM", portfolio, new Lot(40, 287.0), new Lot(20, 426.0)),
				new OwnedStock("F", portfolio, new Lot(20, 234.0), new Lot(30, 465.0))
				);		
		
		when(ownedStockRepository.findAll()).thenReturn(ownedStocks );

		String url = "/api/portfolio/findOwnedStocksByPortfolio/" + portfolioId;
		setUpMocks(testUserName, portfolioId);
		
		ResponseEntity<OwnedStockDTOListResult> result = makeAuthenticatedGetRequest(url, OwnedStockDTOListResult.class);
		
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		OwnedStockDTOListResult stocksResponseEntity = result.getBody();
		ArrayList<OwnedStockDTO> actualStocks = stocksResponseEntity;
		assertThat(actualStocks.size()).isEqualTo(3);
	}

	/**
	 * Test finding stocks using invalid portfolio Id
	 */
	@Test
	void findStocksByInvalidPortfolio() {
		String testUserName = "TestUser1";
		long portfolioId = 10L;
		when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

		String url = "/api/portfolio/findOwnedStocksByPortfolio/" + portfolioId;
		setUpMocks(testUserName, portfolioId);
		
		ResponseEntity<OwnedStockDTOListResult> result = makeAuthenticatedGetRequest(url, OwnedStockDTOListResult.class);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	/**
	 * Make HTTP Get request to a url with authentication header, parse the response into a Java object.
	 * @param <T> datatype parameter
	 * @param url URL
	 * @param returnDataType return datatype class
	 * @return data from the request
	 */
	private <T> ResponseEntity<T> makeAuthenticatedGetRequest(String url, Class<T> returnDataType) {
		// 
		// https://stackoverflow.com/a/34643205
		HttpHeaders headers = new HttpHeaders();				
		String jwtToken = jwtTokenUtil.generateToken(userDetails);
		headers.add(AUTHORIZATION_HEADER_NAME, "Bearer " + jwtToken);
		return testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), returnDataType);
	}

	private void setUpMocks(String userName, long portfolioId) {
		when(userDetailsService.loadUserByUsername(userName)).thenReturn(userDetails);
		when(userDetails.getUsername()).thenReturn(userName);
		when(userContextService.isPortfolioIdValidFromContext(portfolioId)).thenReturn(true);
	}

	// Using this as a response entity type to work around warnings and generic type limitations.
	static class OwnedStockDTOListResult extends ArrayList<OwnedStockDTO> {
		private static final long serialVersionUID = 5134786708673475645L;
	}
}
