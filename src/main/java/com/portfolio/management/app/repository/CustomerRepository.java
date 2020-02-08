package com.portfolio.management.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.management.app.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	List<Customer> findByEmail(String email);
}