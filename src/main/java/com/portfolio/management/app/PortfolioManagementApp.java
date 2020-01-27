package com.portfolio.management.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioManagementApp {
//	@Value ("${DATABASE_URL}")
//	private String databaseUrl;

	public static void main(String[] args) {
		SpringApplication.run(PortfolioManagementApp.class, args);
	}
//
//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
////			System.out.println("Database url: " + databaseUrl);
//		};    
//	}
}
