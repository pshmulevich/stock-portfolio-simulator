package com.portfolio.management.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.repository.LotRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;

/**
 * Spring Boot "unit test". Execution takes much longer than
 * the execution of a true unit test {@link SellServiceTest}.
 * 
 * Avoid using this type of test whenever a true unit test is possible.
 */
@SpringBootTest
class SellServiceSpringBootTest {

	@MockBean
	private LotRepository lotRepository;

	@MockBean
	private OwnedStockRepository ownedStockRepository;

	@Autowired
	private SellService sellService; //what we are testing

	@Test
	void testSellPartial() {
		// create sample lot for testing
		int sharesOwned = 20;
		int numSharesToSell = 19;		
		double purchasePrice = 300.0;
		Lot lot = new Lot(sharesOwned, purchasePrice);

		// this constructor will set back-reference from lot to owned stock
		OwnedStock ownedStock = new OwnedStock("APPL", lot);


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

		// this constructor will set back-reference from lot to owned stock
		OwnedStock ownedStock = new OwnedStock("APPL", lot);
		
		// call method under test
		Optional<Lot> savedLotOptional = sellService.sell(lot, numSharesToSell);

		// assert that a valid lot is present
		assertThat(savedLotOptional.isPresent()).isFalse();

		// verify the delete method was called with proper arg
		verify(lotRepository).delete(lot);

		// assert the lot is gone
		assertThat(ownedStock.getLots().contains(lot)).isFalse();

		// verify that save was called on ownedStockRepository with proper arguments
		verify(ownedStockRepository).save(ownedStock);
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
}
