package net.ememed.user2.entity;

public class PacketBuyInfo {
	private int success;
	private String errormsg;
	private PacketBuyEntry data;
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public PacketBuyEntry getData() {
		return data;
	}
	public void setData(PacketBuyEntry data) {
		this.data = data;
	}
	
	
}
