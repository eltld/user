package net.ememed.user2.entity;

public class AlipayOrderInfo {

	private int success;	
	private String errormsg;
	private AlipayEntry data;
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
	public AlipayEntry getData() {
		return data;
	}
	public void setData(AlipayEntry data) {
		this.data = data;
	}
	

}
