package com.portfolio.management.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.management.app.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
	List<Stock> findByStockSymbol(String stockSymbol);
	List<Stock> findAll();
}