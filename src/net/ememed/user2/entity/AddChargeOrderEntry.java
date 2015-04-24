package net.ememed.user2.entity;

import java.io.Serializable;

public class AddChargeOrderEntry{
	
	private int success;
	private String errormsg;
	private AddChargeOrderInfo data;
	
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
	public AddChargeOrderInfo getData() {
		return data;
	}
	public void setData(AddChargeOrderInfo data) {
		this.data = data;
	}
}
