package com.portfolio.management.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.portfolio.management.app.dto.ErrorDTO;

/**
 * Suppresses default error page to prevent disclosing internal stacktraces.
 * 
 */
//@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExceptionController implements ErrorController {
	  
    private static final String PATH = "/error";
     
    @GetMapping(value=PATH)
    public ResponseEntity<ErrorDTO> error() {
        return new ResponseEntity<>(new ErrorDTO("Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
 
    @Override
    public String getErrorPath() {
        return PATH;
    }
}