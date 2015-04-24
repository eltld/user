package net.ememed.user2.medicalhistory.bean;

import java.util.List;

public class MedicalHistoryBean {
	private int success;
	private int count;
	private String errormsg;
	private List<MedicalHistory> data;

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
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
	public List<MedicalHistory> getData() {
		return data;
	}
	public void setData(List<MedicalHistory> data) {
		this.data = data;
	}
	
}
