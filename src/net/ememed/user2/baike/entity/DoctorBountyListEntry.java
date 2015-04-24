package net.ememed.user2.baike.entity;

import java.util.List;

public class DoctorBountyListEntry {
	private int success;
	private String errormsg;
	private List<DoctorBountyListInfo> data;
	private int pages;
	private int total_bounty_num;
	private int count;
	
	
	
	public int getTotal_bounty_num() {
		return total_bounty_num;
	}
	public void setTotal_bounty_num(int total_bounty_num) {
		this.total_bounty_num = total_bounty_num;
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
	public List<DoctorBountyListInfo> getData() {
		return data;
	}
	public void setData(List<DoctorBountyListInfo> data) {
		this.data = data;
	}
	
	
}
