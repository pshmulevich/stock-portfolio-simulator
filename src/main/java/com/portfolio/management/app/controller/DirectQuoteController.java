package com.portfolio.management.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Controller for exposing direct quotes over REST API
 *
 */
@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DirectQuoteController {
	@Value("${quote.api.token}")
	private String apiToken;

	private final RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/quote/{stockSymbol}")
	public StockQuotes getQuote(@PathVariable("stockSymbol") String stockSymbol){
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("api_token", apiToken);
		uriVariables.put("symbol", stockSymbol);

		ResponseEntity<StockQuotes> response = restTemplate.getForEntity("https://api.worldtradingdata.com/api/v1/stock?symbol={symbol}&api_token={api_token}", StockQuotes.class, uriVariables);
		return response.getBody();
	}
	
	/**
	 * Use this endpoint for debugging to get the unparsed response string from api.worldtradingdata.com.
	 * @param stockSymbol stock symbol
	 * @return unparsed service response
	 */
	@GetMapping("/quoteDebug/{stockSymbol}")
	public String getQuoteDebug(@PathVariable("stockSymbol") String stockSymbol){
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("api_token", apiToken);
		uriVariables.put("symbol", stockSymbol);

		ResponseEntity<String> response = restTemplate.getForEntity("https://api.worldtradingdata.com/api/v1/stock?symbol={symbol}&api_token={api_token}", String.class, uriVariables);
		return response.getBody();
	}

	public static class StockQuotes {
		private float symbols_requested;
		private float symbols_returned;
		private ArrayList <StockQuote> data = new ArrayList <StockQuote> ();
		private String message;

		public float getSymbols_requested() {
			return symbols_requested;
		}

		public float getSymbols_returned() {
			return symbols_returned;
		}
 
		public void setSymbols_requested(float symbols_requested) {
			this.symbols_requested = symbols_requested;
		}

		public void setSymbols_returned(float symbols_returned) {
			this.symbols_returned = symbols_returned;
		}

		public ArrayList < StockQuote > getData() {
			return data;
		}

		public void setData(ArrayList < StockQuote > data) {
			this.data = data;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	public static class StockQuote {
		private String symbol;
		private String name;
		private String currency;
		private String price;
		private String price_open;
		private String day_high;
		private String day_low;
		private String day_change;
		private String change_pct;
		private String close_yesterday;
		private String market_cap;
		private String volume;
		private String volume_avg;
		private String shares;
		private String stock_exchange_long;
		private String stock_exchange_short;
		private String timezone;
		private String timezone_name;
		private String gmt_offset;
		private String last_trade_time;
		private String pe;
		private String eps;

		public String getSymbol() {
			return symbol;
		}

		public String getName() {
			return name;
		}

		public String getCurrency() {
			return currency;
		}

		public String getPrice() {
			return price;
		}

		public String getPrice_open() {
			return price_open;
		}

		public String getDay_high() {
			return day_high;
		}

		public String getDay_low() {
			return day_low;
		}

		public String getDay_change() {
			return day_change;
		}

		public String getChange_pct() {
			return change_pct;
		}

		public String getClose_yesterday() {
			return close_yesterday;
		}

		public String getMarket_cap() {
			return market_cap;
		}

		public String getVolume() {
			return volume;
		}

		public String getVolume_avg() {
			return volume_avg;
		}

		public String getShares() {
			return shares;
		}

		public String getStock_exchange_long() {
			return stock_exchange_long;
		}

		public String getStock_exchange_short() {
			return stock_exchange_short;
		}

		public String getTimezone() {
			return timezone;
		}

		public String getTimezone_name() {
			return timezone_name;
		}

		public String getGmt_offset() {
			return gmt_offset;
		}

		public String getLast_trade_time() {
			return last_trade_time;
		}

		public String getPe() {
			return pe;
		}

		public String getEps() {
			return eps;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public void setPrice_open(String price_open) {
			this.price_open = price_open;
		}

		public void setDay_high(String day_high) {
			this.day_high = day_high;
		}

		public void setDay_low(String day_low) {
			this.day_low = day_low;
		}

		public void setDay_change(String day_change) {
			this.day_change = day_change;
		}

		public void setChange_pct(String change_pct) {
			this.change_pct = change_pct;
		}

		public void setClose_yesterday(String close_yesterday) {
			this.close_yesterday = close_yesterday;
		}

		public void setMarket_cap(String market_cap) {
			this.market_cap = market_cap;
		}

		public void setVolume(String volume) {
			this.volume = volume;
		}

		public void setVolume_avg(String volume_avg) {
			this.volume_avg = volume_avg;
		}

		public void setShares(String shares) {
			this.shares = shares;
		}

		public void setStock_exchange_long(String stock_exchange_long) {
			this.stock_exchange_long = stock_exchange_long;
		}

		public void setStock_exchange_short(String stock_exchange_short) {
			this.stock_exchange_short = stock_exchange_short;
		}

		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}

		public void setTimezone_name(String timezone_name) {
			this.timezone_name = timezone_name;
		}

		public void setGmt_offset(String gmt_offset) {
			this.gmt_offset = gmt_offset;
		}

		public void setLast_trade_time(String last_trade_time) {
			this.last_trade_time = last_trade_time;
		}

		public void setPe(String pe) {
			this.pe = pe;
		}

		public void setEps(String eps) {
			this.eps = eps;
		}
	}
}
