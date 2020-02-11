package com.portfolio.management.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.entity.Portfolio;

public interface OwnedStockRepository extends JpaRepository<OwnedStock, Long>{
	List<OwnedStock> findByPortfolio(Portfolio portfolio);
	Optional<OwnedStock> findByStockSymbolAndPortfolio(String stockSymbol, Portfolio portfolio);
}
