package net.ememed.user2.entity;

public class PacketPeriodList {
	private String packet_daytype;
	private String packet_period_id;
	private String packet_daynum;
	private String packet_period_price;
	private String packet_period_desc;
	public String getPacket_daytype() {
		return packet_daytype;
	}
	public String getPacket_period_id() {
		return packet_period_id;
	}
	public String getPacket_daynum() {
		return packet_daynum;
	}
	public String getPacket_period_price() {
		return packet_period_price;
	}
	public String getPacket_period_desc() {
		return packet_period_desc;
	}
	public void setPacket_daytype(String packet_daytype) {
		this.packet_daytype = packet_daytype;
	}
	public void setPacket_period_id(String packet_period_id) {
		this.packet_period_id = packet_period_id;
	}
	public void setPacket_daynum(String packet_daynum) {
		this.packet_daynum = packet_daynum;
	}
	public void setPacket_period_price(String packet_period_price) {
		this.packet_period_price = packet_period_price;
	}
	public void setPacket_period_desc(String packet_period_desc) {
		this.packet_period_desc = packet_period_desc;
	}

}
