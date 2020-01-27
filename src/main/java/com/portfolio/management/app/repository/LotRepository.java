package com.portfolio.management.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;

public interface LotRepository extends JpaRepository<Lot, Integer> {

	List<Lot> findLotByOwnedStock(OwnedStock ownedStock);
}
