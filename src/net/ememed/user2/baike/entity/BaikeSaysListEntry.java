package net.ememed.user2.baike.entity;

import java.util.List;

public class BaikeSaysListEntry {
	private int success;
	private String errormsg;
	private List<BaikeSaysInfo> data;
	private int count;
	private int curpage;
	private int pages;
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
	public List<BaikeSaysInfo> getData() {
		return data;
	}
	public void setData(List<BaikeSaysInfo> data) {
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
}	
