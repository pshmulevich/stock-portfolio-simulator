package com.portfolio.management.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.management.app.dto.LotDTO;
import com.portfolio.management.app.dto.OwnedStockDTO;
import com.portfolio.management.app.dto.OwnedStockWithLotsDTO;
import com.portfolio.management.app.entity.Lot;
import com.portfolio.management.app.entity.OwnedStock;
import com.portfolio.management.app.repository.LotRepository;
import com.portfolio.management.app.repository.OwnedStockRepository;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PortfolioController {
	
	@Autowired
	LotRepository lotRepository;
	
	@Autowired
	OwnedStockRepository ownedStockRepository;
	
	@GetMapping("/ownedStocksCreate")
	public String bulkcreate(){
		// save a list of owned Stocks
    	ownedStockRepository.saveAll(Arrays.asList(
    			new OwnedStock("APPL", new Lot(10, 300.0), new Lot(50, 314.0)),
    			new OwnedStock("CRM", new Lot(40, 230.0), new Lot(20, 426.0))
			));

				
		return "Stocks are created";
	}

	@GetMapping("/findAllStocks")
	public List<OwnedStockDTO> findAllStocks(){
		List<OwnedStock> ownedStocks = ownedStockRepository.findAll();
		List<OwnedStockDTO> ownedStockDTOs = new ArrayList<>();
		for(OwnedStock ownedStock : ownedStocks) {			
			ownedStockDTOs.add(new OwnedStockDTO(ownedStock.getId(), ownedStock.getStockSymbol()));
		}
		return ownedStockDTOs;
	}
	
	@GetMapping("/findAllLots")
	public List<LotDTO> findAllLots(){
		List<Lot> lots = lotRepository.findAll();
		return toLotDTOs(lots);
	}

	@GetMapping("/findLotsByOwnedStockId/{ownedStockId}")
	public List<LotDTO> findLotsByOwnedStockId(@PathVariable("ownedStockId") long ownedStockId){
		Optional<OwnedStock> ownedStockOptional = ownedStockRepository.findById(ownedStockId);
		return findLots(ownedStockOptional);
	}

	//do similar to above but by symbol
	@GetMapping("/findLotsByOwnedStockSymbol/{ownedStockSymbol}")
	public List<LotDTO> findLotsByOwnedStockSymbol(@PathVariable("ownedStockSymbol") String ownedStockSymbol){
		Optional<OwnedStock> ownedStock = ownedStockRepository.findByStockSymbol(ownedStockSymbol);
		return findLots(ownedStock);
	}

	@GetMapping("/findAllOwnedStocksWithLots")
	public List<OwnedStockWithLotsDTO> findAllOwnedStocksWithLots(){
		List<OwnedStock> allOwnedStocks = ownedStockRepository.findAll();
		List<OwnedStockWithLotsDTO> ownedStockWithLotsDTOs = new ArrayList<>();
		for(OwnedStock ownedStock : allOwnedStocks) {
			List<LotDTO> lotDTOs = findLots(ownedStock);
			OwnedStockWithLotsDTO ownedStockWithLotsDTO = new OwnedStockWithLotsDTO(ownedStock.getId(), ownedStock.getStockSymbol(), lotDTOs);
			ownedStockWithLotsDTOs.add(ownedStockWithLotsDTO);
		}

		return ownedStockWithLotsDTOs;
	}
	
	//------------------------------
	@GetMapping("/findStockPerformanceById/{ownedStockId}")
	public List<LotDTO> findStockPerformanceById(@PathVariable("ownedStockId") long ownedStockId){
		Optional<OwnedStock> ownedStockOptional = ownedStockRepository.findById(ownedStockId);
		
		//This needs to change. Needs to show performance graph based on cumulative data.
		// An idea would be to have a graph for different time periods.
		// For now, go with cumulative data for all lots bought.
		// Q: How to do this?
		return findLots(ownedStockOptional);
	}
	//------------------------------

	private List<LotDTO> toLotDTOs(List<Lot> lots) {
		List<LotDTO> lotDTOs = new ArrayList<>();
		for(Lot lot : lots) {			
			OwnedStock ownedStock = lot.getOwnedStock();
			OwnedStockDTO ownedStockDTO = new OwnedStockDTO(ownedStock.getId(), ownedStock.getStockSymbol());
			lotDTOs.add(new LotDTO(lot.getId(), lot.getSharesOwned(), lot.getPurchasePrice(), ownedStockDTO));
		}
		return lotDTOs;
	}

	/**
	 * Finds lots for owned stock optional
	 * @param ownedStockOptional optional owned stock object
	 * @return list of LotDTO's if the stock is present otherwise return empty list.
	 */
	private List<LotDTO> findLots(Optional<OwnedStock> ownedStockOptional) {
		if(ownedStockOptional.isPresent()) {
			return findLots(ownedStockOptional.get());
		}

		return Collections.emptyList();
	}

	/**
	 * Finds lots for owned stock
	 * @param ownedStock owned stock object
	 * @return list of LotDTO's
	 */
	private List<LotDTO> findLots(OwnedStock ownedStock) {
		List<Lot> lots = lotRepository.findLotByOwnedStock(ownedStock);
		return toLotDTOs(lots);
	}
}
