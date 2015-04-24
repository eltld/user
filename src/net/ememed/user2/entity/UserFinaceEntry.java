package net.ememed.user2.entity;

import java.io.Serializable;

public class UserFinaceEntry{

	private int success;
	private String errormsg;
	private UserFinaceInfo data;
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
	public UserFinaceInfo getData() {
		return data;
	}
	public void setData(UserFinaceInfo data) {
		this.data = data;
	}

}
