package net.ememed.user2.entity;

import java.util.List;

public class SearchHospitalEn {
	private String success;
	private String errormsg; 
	private String count;
	private String pages;
	private String curpage;
	private List<SearchHospital> data;
	public String getSuccess() {
		return success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public String getCount() {
		return count;
	}
	public String getPages() {
		return pages;
	}
	public String getCurpage() {
		return curpage;
	}
	public List<SearchHospital> getData() {
		return data;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public void setCurpage(String curpage) {
		this.curpage = curpage;
	}
	public void setData(List<SearchHospital> data) {
		this.data = data;
	}
}
