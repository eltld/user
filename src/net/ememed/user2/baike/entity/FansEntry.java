package net.ememed.user2.baike.entity;

import java.util.List;

public class FansEntry{
	
	private int success;
	private String errormsg;
	private List<FansData> data;
	private int count;
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
	public List<FansData> getData() {
		return data;
	}
	public void setData(List<FansData> data) {
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
}
