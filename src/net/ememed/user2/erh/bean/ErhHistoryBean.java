package net.ememed.user2.erh.bean;

import java.util.List;

public class ErhHistoryBean {
	private int success;
	private String errormsg;
	private List<ErhDiseaseHistory> data;

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
	public List<ErhDiseaseHistory> getData() {
		return data;
	}
	public void setData(List<ErhDiseaseHistory> data) {
		this.data = data;
	}
	
}
