package net.ememed.user2.entity;

import java.util.List;

public class ScheduleInfo {

	private int success;
	private String errormsg;
	private List<ScheduleEntry> data;
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
	public List<ScheduleEntry> getData() {
		return data;
	}
	public void setData(List<ScheduleEntry> data) {
		this.data = data;
	}
	
}
