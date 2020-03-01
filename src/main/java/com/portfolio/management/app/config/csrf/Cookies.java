package com.portfolio.management.app.config.csrf;

import static com.portfolio.management.app.config.jwt.constants.Constants.ACCES_CONTROL_EXPOSE_HEADERS_NAME;
import static com.portfolio.management.app.config.jwt.constants.Constants.XSRF_TOKEN_NAME;
import static com.portfolio.management.app.config.jwt.constants.Constants.X_XSRF_TOKEN_NAME;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.util.WebUtils;

public final class Cookies {

	/** Ensures that if a request does not supply a CSRF token in a cookie, or
	 * if the token is not up-to-date, we set it in our response so that subsequent
	 * requests can succeed. */
	public static void setSecurityTokens(HttpServletRequest request, HttpServletResponse response) {
		CsrfToken csrf = (CsrfToken) request.getAttribute(X_XSRF_TOKEN_NAME);
				
		if (csrf != null) {
			Cookie cookie = WebUtils.getCookie(request, XSRF_TOKEN_NAME);
			String token = csrf.getToken();
			if (cookie == null || token != null && !token.equals(cookie.getValue())) {
				cookie = new Cookie(XSRF_TOKEN_NAME, token);
				cookie.setPath("/");
				cookie.setHttpOnly(false);
				response.addCookie(cookie);
				
				response.addHeader(X_XSRF_TOKEN_NAME, token);
				response.addHeader(ACCES_CONTROL_EXPOSE_HEADERS_NAME, X_XSRF_TOKEN_NAME);
			}
		}
	}
}