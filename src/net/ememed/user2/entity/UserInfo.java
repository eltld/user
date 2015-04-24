package net.ememed.user2.entity;

public class UserInfo {
	private int success;
	private String errormsg;
	private UserInfoEntry data;
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
	public UserInfoEntry getData() {
		return data;
	}
	public void setData(UserInfoEntry data) {
		this.data = data;
	}
}
