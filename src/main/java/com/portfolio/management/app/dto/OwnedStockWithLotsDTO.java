package com.portfolio.management.app.dto;

import java.util.List;

public class OwnedStockWithLotsDTO extends OwnedStockDTO {
	private List<LotDTO> lotDTOs;
	
	public OwnedStockWithLotsDTO(long id, String stockSymbol, List<LotDTO> lotDTOs) {
		super(id, stockSymbol);
		this.setLotDTOs(lotDTOs);
	}
	public List<LotDTO> getLotDTOs() {
		return lotDTOs;
	}
	public void setLotDTOs(List<LotDTO> lotDTOs) {
		this.lotDTOs = lotDTOs;
	}
}
