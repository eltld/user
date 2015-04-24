package net.ememed.user2.baike.entity;

public class SaysDetailEntry {
	private int success;
	private String errormsg;
	private BaikeSaysInfo data;
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
	public BaikeSaysInfo getData() {
		return data;
	}
	public void setData(BaikeSaysInfo data) {
		this.data = data;
	}
}
