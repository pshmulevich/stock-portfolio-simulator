package com.portfolio.management.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.management.app.entity.OwnedStock;

public interface OwnedStockRepository extends JpaRepository<OwnedStock, Long>{

	Optional<OwnedStock> findByStockSymbol(String ownedStockSymbol);
}
