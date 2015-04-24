package net.ememed.user2.entity;

import java.util.List;

public class HospitalsInfo {

	private int success;
	private String errormsg;
	private HospitalsEntry data;
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
	public HospitalsEntry getData() {
		return data;
	}
	public void setData(HospitalsEntry data) {
		this.data = data;
	}

	
	
}
