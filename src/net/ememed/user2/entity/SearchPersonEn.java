package net.ememed.user2.entity;

import java.util.List;

public class SearchPersonEn {

	private String count;
	private String pages;
	private String curpage;
	private String success;
	private String errormsg;
	private List<SearchPerson> data;
	public String getCount() {
		return count;
	}
	public String getPages() {
		return pages;
	}
	public String getCurpage() {
		return curpage;
	}
	public String getSuccess() {
		return success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public List<SearchPerson> getData() {
		return data;
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
	public void setSuccess(String success) {
		this.success = success;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public void setData(List<SearchPerson> data) {
		this.data = data;
	}
	
	
}
