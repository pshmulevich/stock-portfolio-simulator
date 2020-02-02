package com.portfolio.management.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
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
import com.portfolio.management.app.repository.LotRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PortfolioControllerTest {

	@MockBean
	private LotRepository lotRepository;

	@MockBean
	private OwnedStockRepository ownedStockRepository;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void testFindAllLots() {
		List<Lot> lots = new ArrayList<>();
		lots.add(new Lot(12, 100.0));
		lots.add(new Lot(23, 200.0));
		lots.add(new Lot(34, 300.0));
		new OwnedStock("CRM", lots.toArray(new Lot[0]));
		when(lotRepository.findAll()).thenReturn(lots );


		String url = "/api/portfolio/findAllLots";
		ResponseEntity<LotDTOListResult> result = testRestTemplate.getForEntity(url , LotDTOListResult.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		LotDTOListResult actualLots = result.getBody();
		assertThat(actualLots.size()).isEqualTo(3);
	}

	@Test
	void testFindAllStocks() {
		List<OwnedStock> ownedStocks = Arrays.asList(
				new OwnedStock("AAPL", new Lot(10, 300.0), new Lot(50, 314.0)),
				new OwnedStock("CRM", new Lot(40, 287.0), new Lot(20, 426.0)),
				new OwnedStock("F", new Lot(20, 234.0), new Lot(30, 465.0))
				);

		when(ownedStockRepository.findAll()).thenReturn(ownedStocks );


		String url = "/api/portfolio/findAllStocks";
		ResponseEntity<OwnedStockDTOListResult> result = testRestTemplate.getForEntity(url , OwnedStockDTOListResult.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		OwnedStockDTOListResult actualLots = result.getBody();
		assertThat(actualLots.size()).isEqualTo(3);
	}

	// Using this as a response entity type to work around warnings and generic type limitations.
	static class LotDTOListResult extends ArrayList<LotDTO> {
		private static final long serialVersionUID = 3905054749184260251L;
	}
	
	static class OwnedStockDTOListResult extends ArrayList<OwnedStockDTO> {
		private static final long serialVersionUID = 5134786708673475645L;
	}
}
