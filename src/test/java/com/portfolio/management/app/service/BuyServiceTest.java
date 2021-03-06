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
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.OwnedStockRepository;
import com.portfolio.management.app.repository.PortfolioRepository;

class BuyServiceTest {

	private OwnedStockRepository ownedStockRepository = Mockito.mock(OwnedStockRepository.class);
	
	private PortfolioRepository portfolioRespository = Mockito.mock(PortfolioRepository.class);

	private BuyService buyService; //what we are testing

	@BeforeEach
	void beforeEach() {
		buyService = new BuyService(ownedStockRepository, portfolioRespository);
	}

	@Test
	void testBuyNewStock() {
		int numSharesToBuy = 5;		
		String stockSymbol = "CRM";	
		double currentStockPrice = 158.0;
		long portfolioId = 10L;
		
		mockPortfolio(portfolioId);
		
		// set desired behavior for Mock lot repository
		when(ownedStockRepository.findByStockSymbolAndPortfolio(any(String.class), any(Portfolio.class))).thenReturn(Optional.empty());

		// call method under test
		Lot newLotBought = buyService.buyMarket(portfolioId, stockSymbol, numSharesToBuy, currentStockPrice);
		assertThat(newLotBought.getSharesOwned()).isEqualTo(numSharesToBuy);
		assertThat(newLotBought.getPurchasePrice()).isEqualTo(currentStockPrice);
		assertThat(newLotBought.getSharesOwned()).isEqualTo(numSharesToBuy);
		OwnedStock ownedStock = newLotBought.getOwnedStock();
		assertThat(ownedStock).isNotNull();
		assertThat(ownedStock.getStockSymbol()).isEqualTo(stockSymbol);
		assertThat(ownedStock.getPortfolio().getId()).isEqualTo(portfolioId);

	}

	@Test
	void testBuyExistingStock() {
		int numSharesToBuy = 15;		
		String stockSymbol = "CRM";
		double currentStockPrice = 316.0;
		long portfolioId = 10L;
		
		Portfolio portfolio = mockPortfolio(portfolioId);
		

		OwnedStock existingOwnedStock = new OwnedStock("CRM", portfolio, new Lot(10, 300.0), new Lot(50, 314.0));
		// set desired behavior for Mock lot repository
		when(ownedStockRepository.findByStockSymbolAndPortfolio(any(String.class), any(Portfolio.class))).thenReturn(Optional.of(existingOwnedStock));

		// call method under test
		Lot newLotBought = buyService.buyMarket(portfolioId, stockSymbol, numSharesToBuy, currentStockPrice);
		assertThat(newLotBought.getSharesOwned()).isEqualTo(numSharesToBuy);
		assertThat(newLotBought.getPurchasePrice()).isEqualTo(currentStockPrice);
		assertThat(newLotBought.getSharesOwned()).isEqualTo(numSharesToBuy);
		OwnedStock ownedStock = newLotBought.getOwnedStock();
		assertThat(ownedStock).isNotNull();
		assertThat(ownedStock.getStockSymbol()).isEqualTo(stockSymbol);
		assertThat(ownedStock).isEqualTo(existingOwnedStock);
		assertThat(existingOwnedStock.getLots().size()).isEqualTo(3);
		assertThat(portfolio.getId()).isEqualTo(portfolioId);
	}
	
	@Test
	void testBuyZero() {
		int numSharesToBuy = 0;		
		String stockSymbol = "CRM";
		double currentStockPrice = 300.0;
		long portfolioId = 10L;

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(portfolioId, stockSymbol, numSharesToBuy, currentStockPrice);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "numSharesToBuy cannot be zero or negative: " + numSharesToBuy;
		assertThat(actualMessage.contains(expectedMessage)).isTrue();
	}

	@Test
	void testBuyNegative() {
		int numSharesToBuy = -1;		
		String stockSymbol = "AAPL";
		double currentStockPrice = 558.0;
		long portfolioId = 10L;
		
		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(portfolioId, stockSymbol, numSharesToBuy, currentStockPrice);			
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
		double currentStockPrice = 18.0;
		long portfolioId = 10L;
		
		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(portfolioId, stockSymbol, numSharesToBuy, currentStockPrice);			
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
		double currentStockPrice = 8.0;
		long portfolioId = 10L;
		
		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			buyService.buyMarket(portfolioId, stockSymbol, numSharesToBuy, currentStockPrice);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "stockSymbol cannot be null or empty";
		assertThat(actualMessage).isEqualTo(expectedMessage);
	}

	private Portfolio mockPortfolio(long portfolioId) {
		Portfolio portfolio = Mockito.mock(Portfolio.class);
		when(portfolio.getId()).thenReturn(portfolioId);
		// set appropriate behavior for portfolio repository
		when(portfolioRespository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
		return portfolio;
	}
}
