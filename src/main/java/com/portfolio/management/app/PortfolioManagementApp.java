package com.portfolio.management.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Main Spring Boot Application class.
 *
 */
// could also use spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PortfolioManagementApp {
	public static void main(String[] args) {
		try {
			SpringApplication.run(PortfolioManagementApp.class, args);
		} catch (Throwable e) {
			// see: https://stackoverflow.com/a/60041916
	        if(e.getClass().getName().contains("SilentExitException")) {
	            // ignore
	        } else {
	            throw e;
	        }
	    }
	}
}
