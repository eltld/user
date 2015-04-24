package net.ememed.user2.entity;

import java.util.ArrayList;
import java.util.List;

public class HospitalRoomInfo {

	private int success;
	private String errormsg;
	private List<RoomEntry> data;
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
	public List<RoomEntry> getData() {
		return data;
	}
	public void setData(List<RoomEntry> data) {
		this.data = data;
	}
	
}
