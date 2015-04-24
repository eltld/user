package net.ememed.user2.entity;

import java.util.List;

public class HospInfo {
	
	 private int success;//": 1,
	 private String errormsg;//": "获取成功",
	 private List<HospEntry> data;//":
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
	public List<HospEntry> getData() {
		return data;
	}
	public void setData(List<HospEntry> data) {
		this.data = data;
	}
	 
	 

}
