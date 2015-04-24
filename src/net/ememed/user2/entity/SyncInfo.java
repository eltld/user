package net.ememed.user2.entity;

public class SyncInfo {
	private int success;
	private String errormsg;
	private Citys data;
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
	public Citys getData() {
		return data;
	}
	public void setData(Citys data) {
		this.data = data;
	}

}
