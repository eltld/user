package net.ememed.user2.entity;

import java.io.Serializable;

public class UserBalance implements Serializable{
	private int success;
	private String errormsg;
	private UserBalanceEntry data;
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
	public UserBalanceEntry getData() {
		return data;
	}
	public void setData(UserBalanceEntry data) {
		this.data = data;
	}
	
	
}
