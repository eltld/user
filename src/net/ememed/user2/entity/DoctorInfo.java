package net.ememed.user2.entity;

import java.util.ArrayList;
import java.util.List;

public class DoctorInfo {
	private int success;	
	private String errormsg;
	private int pages;
	private List<DoctorEntry> data;
	private String count;
	private String total_cost;
	
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getTotal_cost() {
		return total_cost;
	}
	public void setTotal_cost(String total_cost) {
		this.total_cost = total_cost;
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
	public List<DoctorEntry> getData() {
		return data;
	}
	public void setData(List<DoctorEntry> data) {
		this.data = data;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	
	
}
