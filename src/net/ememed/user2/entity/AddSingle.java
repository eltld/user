package net.ememed.user2.entity;

public class AddSingle {
	private int success;
	private String errormsg;
	private AddSingleEntry data;
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
	public AddSingleEntry getData() {
		return data;
	}
	public void setData(AddSingleEntry data) {
		this.data = data;
	}
	
}
