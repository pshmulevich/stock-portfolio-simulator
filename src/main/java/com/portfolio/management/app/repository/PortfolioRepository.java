package com.portfolio.management.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.management.app.entity.Account;
import com.portfolio.management.app.entity.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long>{
	Optional<Portfolio> findByName(String name);
	List<Portfolio> findByAccount(Account account);
}