package com.portfolio.management.app.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.portfolio.management.app.entity.Customer;
import com.portfolio.management.app.repository.CustomerRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private CustomerRepository customerRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Customer> customerOptional = customerRepository.findByUserName(username);
		if(!customerOptional.isPresent()) {
			throw new UsernameNotFoundException(username);
		} else {
			System.out.println("User found: " + username);
		}
		return new UserDetailsImpl(username, customerOptional.get().getPassword());
    }
    
    public static class UserDetailsImpl implements UserDetails {
		private static final long serialVersionUID = 6722212544240305703L;
		private final String username;
		private final String password;

		public UserDetailsImpl(String username, String password) {
    		this.username = username;
    		this.password = password;
    	}
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return Collections.emptyList();
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
    	
    }
}
