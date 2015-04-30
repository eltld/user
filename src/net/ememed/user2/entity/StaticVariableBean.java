package net.ememed.user2.entity;

import java.util.List;

public class StaticVariableBean {
	private String success;
	private String errormsg;
	private List<VariableBean> data;
	public String getSuccess() {
		return success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public List<VariableBean> getData() {
		return data;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public void setData(List<VariableBean> data) {
		this.data = data;
	}
	
	
}
