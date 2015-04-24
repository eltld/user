package net.ememed.user2.baike.entity;

public class RewardPayEntry {
	private int success;
	private String errormsg;
	private RewardPayInfo data;
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
	public RewardPayInfo getData() {
		return data;
	}
	public void setData(RewardPayInfo data) {
		this.data = data;
	}
	
}
