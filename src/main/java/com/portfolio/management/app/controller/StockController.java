package com.portfolio.management.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.management.app.dto.StockDTO;
import com.portfolio.management.app.entity.Stock;
import com.portfolio.management.app.repository.StockRepository;

@RestController
public class StockController {
	@Autowired
	StockRepository repository;
	@GetMapping("/stockscreate")
	public String bulkcreate(){
		// save a list of Stocks
		//stockSymbol, openValue, dayRange, dividendRate, peRatio, previousClose, volume, shares
		repository.saveAll(Arrays.asList(
				new Stock("CRM", //stockSymbol
						345.25, //openValue
						43.0, //dayRange
						3.42, //dividendRate
						32.11, //peRatio
						340.75, //previousClose
						122100 //shares
						),
				new Stock("APPL", 
						318.75, 
						12.23, 
						3.08, 
						26.71, 
						316.57, 
						23012),
				new Stock("F", 
						9.11, 
						0.98, 
						0.6, 
						24.52, 
						8.56, 
						396789),
				new Stock("INTC", 
						66.57, 
						8.34, 
						1.26, 
						14.83, 
						65.11, 
						435674)
				));
				
		return "Stocks are created";
	}
	@PostMapping("/createstock")
	public String create(@RequestBody StockDTO stock){
		// save a single Stock
		repository.save(new Stock(stock.getStockSymbol(), 
				stock.getOpenValue(),
				stock.getDayRange(),
				stock.getDividendRate(),
				stock.getPeRatio(),
				stock.getPreviousClose(),
				stock.getVolume()
				));
		return "Stock is created";
	}
	@GetMapping("/findallstock")
	public List<StockDTO> findAll(){
		List<Stock> stocks = repository.findAll();
		List<StockDTO> stockUI = new ArrayList<>();
		for (Stock stock : stocks) {
			stockUI.add(new StockDTO(
					stock.getStockSymbol(), 
					stock.getOpenValue(),
					stock.getDayRange(),
					stock.getDividendRate(),
					stock.getPeRatio(),
					stock.getPreviousClose(),
					stock.getVolume()
					));
		}
		return stockUI;
	}
	
	//---------------------------------------------
	@GetMapping("/clearallstock")
	public void clearAll(){
		repository.deleteAll();;		
	}
	//--------------------------------------------
	@RequestMapping("/searchstock/{id}")
	public String search(@PathVariable long id){
		String stock = "";
		stock = repository.findById(id).toString();
		return stock;
	}
	@RequestMapping("/searchbyStockSymbol/{stockSymbol}")
	public List<StockDTO> fetchDataByStockSymbol(@PathVariable String stockSymbol){
		List<Stock> stocks = repository.findByStockSymbol(stockSymbol);
		List<StockDTO> stockUI = new ArrayList<>();
		for (Stock stock : stocks) {
			stockUI.add(new StockDTO(
					stock.getStockSymbol(),
					stock.getOpenValue(),
					stock.getDayRange(),
					stock.getDividendRate(),
					stock.getPeRatio(),
					stock.getPreviousClose(),
					stock.getVolume()
					
					));
		}
		return stockUI;
	}
}
