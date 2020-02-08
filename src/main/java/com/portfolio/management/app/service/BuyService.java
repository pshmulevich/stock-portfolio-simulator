package com.portfolio.management.app.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.repository.OwnedStockRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service to buy stock
 *
 */
@Service
@RequiredArgsConstructor
public class BuyService {
	
	private final OwnedStockRepository ownedStockRepository;
	
	public Lot buyMarket(String stockSymbol, int numSharesToBuy, double currentStockPrice) {
		// TODO: Using stock price coming from client, this price will be slightly out of date
		double marketPrice = currentStockPrice;
		
		Assert.hasLength(stockSymbol, "stockSymbol cannot be null or empty");
		Assert.isTrue(numSharesToBuy > 0, "numSharesToBuy cannot be zero or negative: " + numSharesToBuy);
		
		Optional<OwnedStock> ownedStockOptional = ownedStockRepository.findByStockSymbol(stockSymbol);
		
		Lot lot = new Lot(numSharesToBuy, marketPrice);
		if(ownedStockOptional.isPresent()) {
			// already have lots of this stock
			OwnedStock ownedStock = ownedStockOptional.get();
			Set<Lot> lots = ownedStock.getLots();
			
			lot.setOwnedStock(ownedStock);
			lots.add(lot);
			ownedStockRepository.save(ownedStock);
		} else {
			// buying lot of this new stock
			OwnedStock ownedStock = new OwnedStock(stockSymbol, lot);
			ownedStockRepository.save(ownedStock);
		}	
		return lot;
	}
}
