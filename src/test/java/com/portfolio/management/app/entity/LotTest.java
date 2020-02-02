package com.portfolio.management.app.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LotTest {

	@Test
	void testSetPurchasePrice() {
		int sharesOwned = 10;
		double purchasePrice = 20.0;		

		Lot newLot = new Lot(sharesOwned, purchasePrice);
		assertThat(newLot.getPurchasePrice()).isNotNull();
		assertThat(newLot.getPurchasePrice()).isEqualTo(purchasePrice);
	}
	@Test
	void testSetSharesOwned() {
		int sharesOwned = 10;
		double purchasePrice = 20.0;
		
		Lot newLot = new Lot(sharesOwned, purchasePrice);
		assertThat(newLot.getSharesOwned()).isNotNull();
		assertThat(newLot.getSharesOwned()).isEqualTo(sharesOwned);
	}

}
