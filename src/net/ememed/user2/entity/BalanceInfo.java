package net.ememed.user2.entity;

import java.io.Serializable;

public class BalanceInfo implements Serializable{
	private int success;	
	private String errormsg;
	private BalanceEntry data;
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
	public BalanceEntry getData() {
		return data;
	}
	public void setData(BalanceEntry data) {
		this.data = data;
	}
	
}
