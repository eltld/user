package net.ememed.user2.entity;

public class SetProfile {
	private int success;
	private String errormsg;
	private String data;

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
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
}
