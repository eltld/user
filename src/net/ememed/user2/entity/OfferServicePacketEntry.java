package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfferServicePacketEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4180174550446251739L;
	private String SERVICE_CONTENT;
	private String PACKET_ID;
	private List<PacketPeriod> PACKET_PERIOD_LIST;
	private String IS_OFFER;
	
	public String getIS_OFFER() {
		return IS_OFFER;
	}
	public void setIS_OFFER(String iS_OFFER) {
		IS_OFFER = iS_OFFER;
	}
	public String getSERVICE_CONTENT() {
		return SERVICE_CONTENT;
	}
	public void setSERVICE_CONTENT(String sERVICE_CONTENT) {
		SERVICE_CONTENT = sERVICE_CONTENT;
	}
	public String getPACKET_ID() {
		return PACKET_ID;
	}
	public void setPACKET_ID(String pACKET_ID) {
		PACKET_ID = pACKET_ID;
	}
	public List<PacketPeriod> getPACKET_PERIOD_LIST() {
		return PACKET_PERIOD_LIST;
	}
	public void setPACKET_PERIOD_LIST(List<PacketPeriod> pACKET_PERIOD_LIST) {
		PACKET_PERIOD_LIST = pACKET_PERIOD_LIST;
	}
	
}
