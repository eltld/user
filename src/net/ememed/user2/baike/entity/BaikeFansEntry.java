package net.ememed.user2.baike.entity;

public class BaikeFansEntry {
	
	private int success;
	private String errormsg;
	private BaikeFansNum data;
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
	public BaikeFansNum getData() {
		return data;
	}
	public void setData(BaikeFansNum data) {
		this.data = data;
	}
}
