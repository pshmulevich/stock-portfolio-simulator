package com.portfolio.management.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.portfolio.management.app.dto.LotDTO;
import com.portfolio.management.app.dto.OwnedStockDTO;
import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.LotRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;
import com.portfolio.management.app.repository.PortfolioRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PortfolioControllerTest {

	@MockBean
	private LotRepository lotRepository;

	@MockBean
	private OwnedStockRepository ownedStockRepository;

	@MockBean
	private PortfolioRepository portfolioRepository;
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	/**
	 * Test finding all lots
	 */
	@Test
	void testFindAllLots() {
		List<Lot> lots = new ArrayList<>();
		lots.add(new Lot(12, 100.0));
		lots.add(new Lot(23, 200.0));
		lots.add(new Lot(34, 300.0));
		Portfolio portfolio = null;
		new OwnedStock("CRM", portfolio , lots.toArray(new Lot[0]));
		when(lotRepository.findAll()).thenReturn(lots );


		String url = "/api/portfolio/findAllLots";
		ResponseEntity<LotDTOListResult> result = testRestTemplate.getForEntity(url , LotDTOListResult.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		LotDTOListResult actualLots = result.getBody();
		assertThat(actualLots.size()).isEqualTo(3);
	}

	/**
	 * Test finding stocks using specific portfolio Id
	 */
	@Test
	void findStocksByPortfolio() {
		Portfolio portfolio = Mockito.mock(Portfolio.class);
		long portfolioId = 10L;
		when(portfolio.getId()).thenReturn(portfolioId );
		when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
		
		List<OwnedStock> ownedStocks = Arrays.asList(
				new OwnedStock("AAPL", portfolio, new Lot(10, 300.0), new Lot(50, 314.0)),
				new OwnedStock("CRM", portfolio, new Lot(40, 287.0), new Lot(20, 426.0)),
				new OwnedStock("F", portfolio, new Lot(20, 234.0), new Lot(30, 465.0))
				);

		when(ownedStockRepository.findAll()).thenReturn(ownedStocks );

		String url = "/api/portfolio/findStocksByPortfolio/10";
		ResponseEntity<OwnedStockDTOListResult> result = testRestTemplate.getForEntity(url , OwnedStockDTOListResult.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		OwnedStockDTOListResult actualLots = result.getBody();
		assertThat(actualLots.size()).isEqualTo(3);
	}

	/**
	 * Test finding stocks using invalid portfolio Id
	 */
	@Test
	void findStocksByInvalidPortfolio() {
		long portfolioId = 10L;
		when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

		String url = "/api/portfolio/findStocksByPortfolio/" + portfolioId;
		// Make HTTP Get request to the url above, parse the response into a Java object
		ResponseEntity<OwnedStockDTOListResult> result = testRestTemplate.getForEntity(url , OwnedStockDTOListResult.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	// Using this as a response entity type to work around warnings and generic type limitations.
	static class LotDTOListResult extends ArrayList<LotDTO> {
		private static final long serialVersionUID = 3905054749184260251L;
	}
	
	static class OwnedStockDTOListResult extends ArrayList<OwnedStockDTO> {
		private static final long serialVersionUID = 5134786708673475645L;
	}
}
