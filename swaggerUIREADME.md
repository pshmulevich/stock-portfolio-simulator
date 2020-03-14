# How to test the Portfolio Investment Simulator Application through swagger-ui:

This is a Spring Boot RESTful project that provides integration with swagger to visualize the API

1) Navigate to: /swagger-ui.html#/ (e.g., http://localhost:9090/swagger-ui.html#/)

2) Simulate registration of a new user:
* Expand the tab for "Registration and sign-in API" that says "Sign Up Controller"
* Click on the POST for "/api/portfolio/signup" endpoint that says "Register a new customer"
* Click "Try it out" and fill in the placeholders for email, firstname, etc... 
* Click "Execute"

3) Simulate a login to receive the JWT access token.
* Open the POST section for "/api/portfolio/token"
* Here you only need to supply username and password values
* Click "Execute"
* From the response section, copy the token to clipboard

4) Now you can get a quote for a stock symbol:
* Expand the "Direct Quote API" tab
* Select the "GET" section for the "/api/portfolio/quote/{stockSymbol}" endpoint that says "Get quote by stock symbol"
* In the input box that says "Bearer " append the entire token without quotes
* In the input box directly below, enter the ticker symbol for the stock you want to get a quote on
* Click "Execute"
* In the third box in the Responses section you should see the current quote information for the stock symbol you queried 