package net.ememed.user2.entity;

import java.util.List;

public class GuahaoDoctor {

	private int success;// => 1
	private String errormsg;// => 获取成功
	private List<GuahaoDoctorInfo> data;// => Array	有号的
	private List<GuahaoDoctorInfo> non_data;// =>Array 无号的
	private int count;
	private int pages;
	private int curpage;
	
	public List<GuahaoDoctorInfo> getNon_data() {
		return non_data;
	}
	public void setNon_data(List<GuahaoDoctorInfo> non_data) {
		this.non_data = non_data;
	}
	public void setPages(int pages) {
		this.pages = pages;
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
	public List<GuahaoDoctorInfo> getData() {
		return data;
	}
	public void setData(List<GuahaoDoctorInfo> data) {
		this.data = data;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getPages() {
		return pages;
	}
	public void setpages(int pages) {
		this.pages = pages;
	}
	
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
}
