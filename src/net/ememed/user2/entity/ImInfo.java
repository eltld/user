package net.ememed.user2.entity;

import java.util.List;

public class ImInfo {

	private String success;
	private String errormsg;
	private ChatInfo data;
	private String count;
	private String pages;
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	
	
	public ChatInfo getData() {
		return data;
	}
	public void setData(ChatInfo data) {
		this.data = data;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	

	
}
