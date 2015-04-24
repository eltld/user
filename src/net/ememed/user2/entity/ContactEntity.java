package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

public class ContactEntity implements Serializable{
	private int success;
	private String errormsg;
	private List<ContactEntry> data;
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
	public List<ContactEntry> getData() {
		return data;
	}
	public void setData(List<ContactEntry> data) {
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
