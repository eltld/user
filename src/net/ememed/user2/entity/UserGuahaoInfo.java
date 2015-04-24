package net.ememed.user2.entity;

import java.util.List;

public class UserGuahaoInfo {

	private int success;
	private String errormsg;
	private List<UserGuahaoEntry> data;

	private int count;
	private int pages;
	private int curpage;

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

	public List<UserGuahaoEntry> getData() {
		return data;
	}

	public void setData(List<UserGuahaoEntry> data) {
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

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	
	

}
