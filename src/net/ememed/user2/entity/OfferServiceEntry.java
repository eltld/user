package net.ememed.user2.entity;

import java.io.Serializable;

public class OfferServiceEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4131496008514908905L;
	private int IS_OFFER; //=> 0
	private float OFFER_PRICE; //=> -1
	
	public int getIS_OFFER() {
		return IS_OFFER;
	}
	public void setIS_OFFER(int iS_OFFER) {
		this.IS_OFFER = iS_OFFER;
	}
	public float getOFFER_PRICE() {
		return OFFER_PRICE;
	}
	public void setOFFER_PRICE(float oFFER_PRICE) {
		this.OFFER_PRICE = oFFER_PRICE;
	}
	
	
	
}
