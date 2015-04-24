package net.ememed.user2.baike.entity;

public class BaikeBountyEntry {
	private int success;
	private String errormsg;
	private baikeBountyInfo data;
	
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
	public baikeBountyInfo getData() {
		return data;
	}
	public void setData(baikeBountyInfo data) {
		this.data = data;
	}
	
	
}
