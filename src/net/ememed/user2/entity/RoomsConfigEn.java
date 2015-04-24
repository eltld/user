package net.ememed.user2.entity;

import java.io.Serializable;

public class RoomsConfigEn implements Serializable{
	private String success;
	private String errormsg;
	
	private RoomsEn data;
	
	public String getSuccess() {
		return success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public RoomsEn getData() {
		return data;
	}
	public void setData(RoomsEn data) {
		this.data = data;
	}
	
}