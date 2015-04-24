package net.ememed.user2.entity;

public class SeekhelpPic {
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
	public SeekhelpPicEntry getData() {
		return data;
	}
	public void setData(SeekhelpPicEntry data) {
		this.data = data;
	}
	private int success;
	private String errormsg;
	private SeekhelpPicEntry data;
}
