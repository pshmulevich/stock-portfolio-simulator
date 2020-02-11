package com.portfolio.management.app.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;
import com.portfolio.management.app.repository.OwnedStockRepository;
import com.portfolio.management.app.repository.PortfolioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service to buy stock
 *
 */
@Service
@RequiredArgsConstructor // Spring will generate a constructor with arguments to initialize required fields
public class BuyService {

	private final OwnedStockRepository ownedStockRepository;

	private final PortfolioRepository portfolioRespository;

	public Lot buyMarket(long portfolioId, String stockSymbol, int numSharesToBuy, double currentStockPrice) {
		// TODO: Using stock price coming from client, this price will be slightly out of date
		double marketPrice = currentStockPrice;

		Assert.hasLength(stockSymbol, "stockSymbol cannot be null or empty");
		Assert.isTrue(numSharesToBuy > 0, "numSharesToBuy cannot be zero or negative: " + numSharesToBuy);
		
		// Locate portfolio first
		Optional<Portfolio> porfolioOptional = portfolioRespository.findById(portfolioId);
		Assert.isTrue(porfolioOptional.isPresent(), "Portfolio not found. Portfolio Id: " + portfolioId);
		Portfolio portfolio = porfolioOptional.get();
		
		Optional<OwnedStock> ownedStockOptional = ownedStockRepository.findByStockSymbolAndPortfolio(stockSymbol, portfolio);

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
			OwnedStock ownedStock = new OwnedStock(stockSymbol, portfolio , lot);
			ownedStockRepository.save(ownedStock);						
		}	
		return lot;
	}
}
