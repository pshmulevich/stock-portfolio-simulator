package com.portfolio.management.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.LotRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;

class SellServiceTest {

	private LotRepository lotRepository = Mockito.mock(LotRepository.class);
	private OwnedStockRepository ownedStockRepository = Mockito.mock(OwnedStockRepository.class);

	private SellService sellService; //what we are testing

	@BeforeEach
	void beforeEach() {
		sellService = new SellService(lotRepository, ownedStockRepository);
	}

	@Test
	void testSellPartial() {
		// create sample lot for testing
		int sharesOwned = 20;
		int numSharesToSell = 19;		
		double purchasePrice = 300.0;
		Lot lot = new Lot(sharesOwned, purchasePrice);

		// set desired behavior for Mock lot repository
		when(lotRepository.save(any(Lot.class))).then(returnsFirstArg());

		// call method under test
		Optional<Lot> savedLotOptional = sellService.sell(lot, numSharesToSell);

		// assert that a valid lot is present
		assertThat(savedLotOptional.isPresent()).isTrue();
		Lot savedLot = savedLotOptional.get();

		// assert the remaining number of shares is correct
		assertThat(savedLot.getSharesOwned()).isEqualTo(sharesOwned - numSharesToSell);

		// verify the save method was called with proper arg
		verify(lotRepository).save(lot);
	}

	//sell all, then sell more than
	@Test
	void testSellAll() {
		// create sample lot for testing
		int sharesOwned = 20;
		int numSharesToSell = 20;		
		double purchasePrice = 300.0;
		Lot lot = new Lot(sharesOwned, purchasePrice);
		Portfolio portfolio = null;
		// this constructor will set back-reference from lot to owned stock
		OwnedStock ownedStock = new OwnedStock("AAPL", portfolio , lot);

		// call method under test
		Optional<Lot> savedLotOptional = sellService.sell(lot, numSharesToSell);

		// assert that a valid lot is present
		assertThat(savedLotOptional.isPresent()).isFalse();

		// verify that the delete method was called on lotRepository with proper arg
		verify(lotRepository).delete(lot);

		// assert the lot is gone
		assertThat(ownedStock.getLots().contains(lot)).isFalse();

		// verify that delete was called on ownedStockRepository with proper arguments
		// TODO: this assertion is for selling the last lot. 
		// Add similar assertion for save is called instead of delete if there are other remaining lots of this stock
		verify(ownedStockRepository).delete(ownedStock);
	}

	@Test
	void testSellInvalidMore() {
		// try to sell more shares than we have
		int sharesOwned = 20;
		int numSharesToSell = 21;		
		double purchasePrice = 300.0;
		Lot lot = new Lot(sharesOwned, purchasePrice);

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			sellService.sell(lot, numSharesToSell);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "newNumberOfShares cannot be negative: " + (sharesOwned - numSharesToSell);
		assertThat(actualMessage.contains(expectedMessage)).isTrue();
	}

	@Test
	void testSellZero() {
		// create sample lot for testing
		int sharesOwned = 20;
		int numSharesToSell = 0;		
		double purchasePrice = 300.0;
		Lot lot = new Lot(sharesOwned, purchasePrice);

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			sellService.sell(lot, numSharesToSell);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "numSharesToSell cannot be zero or negative: " + numSharesToSell;
		assertThat(actualMessage.contains(expectedMessage)).isTrue();
	}

	@Test
	void testSellNegative() {
		// create sample lot for testing
		int sharesOwned = 20;
		int numSharesToSell = -1;		
		double purchasePrice = 300.0;
		Lot lot = new Lot(sharesOwned, purchasePrice);

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			sellService.sell(lot, numSharesToSell);			
		});

		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "numSharesToSell cannot be zero or negative: " + numSharesToSell;
		assertThat(actualMessage.contains(expectedMessage)).isTrue();
	}

	@Test
	void testLotNull() {
		int numSharesToSell = 20;		

		// assert that the right exception is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			// call method under test
			sellService.sell(null, numSharesToSell);
		});
		// assert that the exception has the right message
		String actualMessage = exception.getMessage();
		String expectedMessage = "lot cannot be null";
		assertThat(actualMessage.contains(expectedMessage)).isTrue();
	}
}
