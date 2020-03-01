package com.portfolio.management.app.config.jwt.constants;

public class Constants {

	public static final String XSRF_TOKEN_NAME = "XSRF-TOKEN";
	public static final String X_XSRF_TOKEN_NAME = "_csrf";
	public static final String ACCES_CONTROL_EXPOSE_HEADERS_NAME = "Access-Control-Expose-Headers";
	
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
}
