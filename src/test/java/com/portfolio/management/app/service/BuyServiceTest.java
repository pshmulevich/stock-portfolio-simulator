package com.portfolio.management.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.repository.OwnedStockRepository;

class BuyServiceTest {

	private OwnedStockRepository ownedStockRepository = Mockito.mock(OwnedStockRepository.class);

	private BuyService buyService; //what we are testing

	@BeforeEach
	void beforeEach() {
		buyService = new BuyService(ownedStockRepository);
	}

	@Test
	void testBuyNewStock() {
		int numSharesToBuy = 5;		
		String stockSymbol = "CRM";	

		// set desired behavior for Mock lot repository
		when(ownedStockRepository.findByStockSymbol(any(String.class))).thenReturn(Optional.empty());

		// call method under test
		Lot newLotBought = buyService.buyMarket(stockSymbol, numSharesToBuy);
		assertThat(newLotBought.getSharesOwned()).isEqualTo(numSharesToBuy);
		OwnedStock ownedStock = newLotBought.getOwnedStock();
		assertThat(ownedStock).isNotNull();
		assertThat(ownedStock.getStockSymbol()).isEqualTo(stockSymbol);
	}


	@Test
	void testBuyExistingStock() {
		int numSharesToBuy = 15;		
		String stockSymbol = "CRM";	

		OwnedStock existingOwnedStock = new OwnedStock("CRM", new Lot(10, 300.0), new Lot(50, 314.0));
		// set desired behavior for Mock lot repository
		when(ownedStockRepository.findByStockSymbol(any(String.class))).thenReturn(Optional.of(existingOwnedStock));

		// call method under test
		Lot newLotBought = buyService.buyMarket(stockSymbol, numSharesToBuy);
		assertThat(newLotBought.getSharesOwned()).isEqualTo(numSharesToBuy);
		OwnedStock ownedStock = newLotBought.getOwnedStock();
		assertThat(ownedStock).isNotNull();
		assertThat(ownedStock.getStockSymbol()).isEqualTo(stockSymbol);
		assertThat(ownedStock).isEqualTo(existingOwnedStock);
		assertThat(existingOwnedStock.getLots().size()).isEqualTo(3);
	}
	
	@Test
	void testBuyZero() {
		int numSharesToBuy = 0;		
		String stockSymbol = "CRM";		

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(stockSymbol, numSharesToBuy);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "numSharesToBuy cannot be zero or negative: " + numSharesToBuy;
		assertThat(actualMessage.contains(expectedMessage)).isTrue();
	}

	@Test
	void testBuyNegative() {
		int numSharesToBuy = -1;		
		String stockSymbol = "APPL";		

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(stockSymbol, numSharesToBuy);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "numSharesToBuy cannot be zero or negative: " + numSharesToBuy;
		assertThat(actualMessage.contains(expectedMessage)).isTrue();
	}

	@Test
	void testBuyNullStockSymbol() {
		int numSharesToBuy = 43;		
		String stockSymbol = null;		

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(stockSymbol, numSharesToBuy);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "stockSymbol cannot be null or empty";
		assertThat(actualMessage).isEqualTo(expectedMessage);
	}
	
	@Test
	void testBuyEmptyStockSymbol() {
		int numSharesToBuy = 43;		
		String stockSymbol = "";		

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(stockSymbol, numSharesToBuy);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "stockSymbol cannot be null or empty";
		assertThat(actualMessage).isEqualTo(expectedMessage);
	}
}
