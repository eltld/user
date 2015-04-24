package net.ememed.user2.entity;

import java.util.List;

public class DoctorInfo2 {
	private int success;
	private String errormsg;
	private List<DoctorEntry2> data;
	private int pages;
	private String count;
	private String curpage;
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
	public List<DoctorEntry2> getData() {
		return data;
	}
	public void setData(List<DoctorEntry2> data) {
		this.data = data;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCurpage() {
		return curpage;
	}
	public void setCurpage(String curpage) {
		this.curpage = curpage;
	}
}
