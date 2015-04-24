package net.ememed.user2.entity;

import java.util.List;

public class GuahaoRecommendDoctor {

	private int success;// => 1
	private String errormsg;// => 获取成功
	private List<GuahaoDoctotItem> data;// => Array
	private int count;

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
	public List<GuahaoDoctotItem> getData() {
		return data;
	}
	public void setData(List<GuahaoDoctotItem> data) {
		this.data = data;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
