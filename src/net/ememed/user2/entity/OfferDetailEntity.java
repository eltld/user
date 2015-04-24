package net.ememed.user2.entity;

public class OfferDetailEntity {
	private String IS_OFFER;
	private String OFFER_PRICE;
	public String getIS_OFFER() {
		return IS_OFFER;
	}
	public void setIS_OFFER(String iS_OFFER) {
		IS_OFFER = iS_OFFER;
	}
	public String getOFFER_PRICE() {
		return OFFER_PRICE;
	}
	public void setOFFER_PRICE(String oFFER_PRICE) {
		OFFER_PRICE = oFFER_PRICE;
	}
	@Override
	public String toString() {
		return "OfferDetailEntity [IS_OFFER=" + IS_OFFER + ", OFFER_PRICE="
				+ OFFER_PRICE + "]";
	}
	
	
	
	
}
