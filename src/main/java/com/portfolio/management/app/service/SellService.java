package com.portfolio.management.app.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.repository.LotRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service to sell stock
 *
 */
@Service
@RequiredArgsConstructor
public class SellService {

	private final LotRepository lotRepository;
	private final OwnedStockRepository ownedStockRepository;

	public Optional<Lot> sell(Lot lot, int numSharesToSell) {
		Assert.notNull(lot, "lot cannot be null");

		Assert.isTrue(numSharesToSell > 0, "numSharesToSell cannot be zero or negative: " + numSharesToSell);
		int oldNumberOfShares = lot.getSharesOwned();
		int newNumberOfShares = oldNumberOfShares - numSharesToSell;
		Assert.isTrue(newNumberOfShares >= 0, "newNumberOfShares cannot be negative: " + newNumberOfShares);

		if(newNumberOfShares == 0) {
			// Upon removing the entire lot, update the owned stock object
			OwnedStock ownedStock = lot.getOwnedStock();
			Set<Lot> ownedStockLots = ownedStock.getLots();
			ownedStockLots.remove(lot);
			// delete the lot entirely
			lotRepository.delete(lot);

			int numRemainingLots = ownedStock.getLots().size();
			if(numRemainingLots > 0) {
				// save
				ownedStockRepository.save(ownedStock);
			} else {
				ownedStockRepository.delete(ownedStock);
			}

			//TODO: need to allocate money from shares sold
			return Optional.empty();
		} else {
			lot.setSharesOwned(newNumberOfShares);
			//TODO: need to allocate money from shares sold
			return Optional.of(lotRepository.save(lot));
		}
	}
}
