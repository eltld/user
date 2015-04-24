package net.ememed.user2.entity;

import java.util.List;

public class OfferPacket {
	private String IS_OFFER;
	private String SERVICE_CONTENT;
	private String PACKET_ID;
	private List<PacketPeriodList> PACKET_PERIOD_LIST;
	public String getIS_OFFER() {
		return IS_OFFER;
	}
	public String getSERVICE_CONTENT() {
		return SERVICE_CONTENT;
	}
	public String getPACKET_ID() {
		return PACKET_ID;
	}
	public List<PacketPeriodList> getPACKET_PERIOD_LIST() {
		return PACKET_PERIOD_LIST;
	}
	public void setIS_OFFER(String iS_OFFER) {
		IS_OFFER = iS_OFFER;
	}
	public void setSERVICE_CONTENT(String sERVICE_CONTENT) {
		SERVICE_CONTENT = sERVICE_CONTENT;
	}
	public void setPACKET_ID(String pACKET_ID) {
		PACKET_ID = pACKET_ID;
	}
	public void setPACKET_PERIOD_LIST(List<PacketPeriodList> pACKET_PERIOD_LIST) {
		PACKET_PERIOD_LIST = pACKET_PERIOD_LIST;
	}

}