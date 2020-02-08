package com.portfolio.management.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.management.app.entity.Account;
import com.portfolio.management.app.entity.Customer;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	List<Account> findByName(String name);
	List<Account> findByCustomer(Customer customer);
}