package com.portfolio.management.app.dto;

public class LotDTO {
	private long id;
	private int sharesOwned;
	private double purchasePrice;
	private OwnedStockDTO ownedStockDTO;

	public LotDTO() {}
	
	public LotDTO(long id, int sharesOwned, double purchasePrice, OwnedStockDTO ownedStockDTO) {
		this.id = id;
		this.sharesOwned = sharesOwned;
		this.purchasePrice = purchasePrice;
		this.ownedStockDTO = ownedStockDTO;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSharesOwned() {
		return sharesOwned;
	}

	public void setSharesOwned(int sharesOwned) {
		this.sharesOwned = sharesOwned;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public OwnedStockDTO getOwnedStockDTO() {
		return ownedStockDTO;
	}

	public void setOwnedStockDTO(OwnedStockDTO ownedStockDTO) {
		this.ownedStockDTO = ownedStockDTO;
	}

	
	
}
