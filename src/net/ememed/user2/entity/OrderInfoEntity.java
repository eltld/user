package net.ememed.user2.entity;

public class OrderInfoEntity {

	private int success;
	private String errormsg;
	private OrderInfoEntry data;

	
	

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

	public OrderInfoEntry getData() {
		return data;
	}

	public void setData(OrderInfoEntry data) {
		this.data = data;
	}

}
